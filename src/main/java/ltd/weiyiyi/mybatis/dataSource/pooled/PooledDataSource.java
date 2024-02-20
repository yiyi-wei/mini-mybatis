package ltd.weiyiyi.mybatis.dataSource.pooled;

import lombok.extern.slf4j.Slf4j;
import ltd.weiyiyi.mybatis.dataSource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 11:34
 * @domain www.weiyiyi.ltd
 */
@Slf4j
public class PooledDataSource implements DataSource {

    private UnpooledDataSource dataSource;

    /**
     * 活跃最大连接数
     */
    protected int poolMaximumActiveConnections = 10;

    /**
     * 空闲最大连接数
     */
    protected int poolMaximumIdleConnections = 5;

    private int expectedConnectionTypeCode;

    /**
     * 是否开启探活模式
     */
    protected boolean poolPingEnabled = false;

    /**
     * 超过这个时间没有被使用过，则需要ping检查
     */
    protected int poolPingConnectionsNotUsedFor = 0;

    /**
     * 测试连通性的SQL语句
     */
    protected String poolPingQuery = "NO PING QUERY SET";


    /**
     * 在被强制返回之前,池中连接被检查的时间
     */
    protected int poolMaximumCheckoutTime = 20000;

    /**
     * 这是给连接池一个打印日志状态机会的低层次设置,还有重新尝试获得连接,
     * 这些情况下往往需要很长时间 为了避免连接池没有配置时静默失败。
     */
    protected int poolTimeToWait = 20000;
    private PoolState poolState = new PoolState(this);

    public PooledDataSource() {
        this.dataSource = new UnpooledDataSource();
    }

    /**
     * 归还连接到连接池
     *
     * @param pooledConnection 归还的连接
     * @throws SQLException sql异常
     */
    protected void pushConnection(PooledConnection pooledConnection) throws SQLException {
        synchronized(poolState) {
            poolState.activeConnections.remove(pooledConnection);
            if(!pooledConnection.isValid()) {
                log.info("A bad connection (" + pooledConnection.getRealHashCode() + ") attempted to " + "return to the pool, discarding connection.");
                poolState.badConnectionCount++;
            }

            // 累计切换耗时
            poolState.accumulatedCheckoutTime += pooledConnection.getCheckoutTime();

            // 现在都是close操作之后了，那么一定是commit之后的操作了，在提交之后的操作都是非法的，应该回滚，保证健壮性
            if(!pooledConnection.getRealConnection().getAutoCommit()) {
                pooledConnection.getRealConnection().rollback();
            }
            // 空闲连接数不足，新增 @2769   41437
            if(poolState.idleConnections.size() < poolMaximumIdleConnections && pooledConnection.getConnectionTypeCode() == expectedConnectionTypeCode) {

                // pooledConnection 实际上是个代理,包一个新的代理，之前的conn就不能要了
                PooledConnection newConnection = new PooledConnection(pooledConnection.getRealConnection(), this);
                poolState.idleConnections.add(newConnection);

                newConnection.setCreatedTimestamp(pooledConnection.getCreatedTimestamp());
                newConnection.setLastUsedTimestamp(pooledConnection.getLastUsedTimestamp());
                pooledConnection.invalidate();
                log.info("Returned connection " + newConnection.getRealHashCode() + " to pool.");

                // 通知其他线程可以来抢DB连接了
                poolState.notifyAll();
            }
            // 空闲连接达到最大,连接充足，push回来的连接直接关闭
            else {
                pooledConnection.getRealConnection().close();
                log.info("Closed connection " + pooledConnection.getRealHashCode() + ".");
                pooledConnection.invalidate();
            }
        }
    }

    /**
     * 检查连接是否孩通的
     *
     * @param connProxy connection proxy
     * @return is pass
     */
    protected boolean pingConnection(PooledConnection connProxy) {
        boolean result;

        try {
            result = !connProxy.getRealConnection().isClosed();
        } catch(SQLException e) {
            log.info("Connection " + connProxy.getRealHashCode() + " is BAD: " + e.getMessage());
            result = false;
        }

        if(!result) {
            return false;
        }

        if(!poolPingEnabled) {
            return true;
        }

        if(poolPingConnectionsNotUsedFor >= 0 && connProxy.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor) {
            try {
                Statement statement = connProxy.getRealConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(poolPingQuery);
                resultSet.close();
                if(!connProxy.getRealConnection().getAutoCommit()) {
                    connProxy.getRealConnection().rollback();
                }
                log.info("Connection " + connProxy.getRealHashCode() + " is GOOD!");
            } catch(SQLException e) {
                log.info("Execution of ping query '" + poolPingQuery + "' failed: " + e.getMessage());
                try {
                    connProxy.getRealConnection().close();
                } catch(SQLException ignore) {
                }
                result = false;
                log.info("Connection " + connProxy.getRealHashCode() + " is BAD: " + e.getMessage());
            }
        }

        return result;
    }

    protected PooledConnection popConnection(String username, String password) throws SQLException {

        /**
         * 是否累加过等待次数
         */
        boolean countedWait = false;

        /**
         * 本地获取失败次数，如果超过空闲连接池size + 3则抛异常
         */
        int localBadConnectionCount = 0;
        PooledConnection conn = null;
        long now = System.currentTimeMillis();

        while(conn == null) {
            synchronized(poolState) {
                // -- 获取conn
                // 1 空闲连接池有，直接返回第一个
                if(!poolState.idleConnections.isEmpty()) {
                    conn = poolState.idleConnections.remove(0);
                    log.info("Checked out connection " + conn.getRealHashCode() + " from pool.");
                } else {
                    // 1. 1 没有，活跃连接小于最大活跃阈值，则新建一个
                    if(poolState.activeConnections.size() < poolMaximumActiveConnections) {
                        // dataSource.getConnection() 约等于new
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        log.info("Created connection " + conn.getRealHashCode() + ".");
                    } else {
                        // 1. 2 没有，活跃连接等于最大活跃阈值，则查看第一个活跃的连接是不是超过了最大checkout时间
                        PooledConnection oldestActiveConn = poolState.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConn.getCheckoutTime();

                        // 1. 2. 1 超过，驱逐并新建，设置poolState中的记录值
                        if(longestCheckoutTime >= poolMaximumCheckoutTime) {
                            poolState.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            poolState.accumulatedCheckoutTime += longestCheckoutTime;
                            poolState.claimedOverdueConnectionCount++;
                            poolState.activeConnections.remove(oldestActiveConn);

                            if(!oldestActiveConn.getRealConnection().getAutoCommit()) {
                                oldestActiveConn.getRealConnection().rollback();
                            }
                            oldestActiveConn.invalidate();
                            conn = new PooledConnection(oldestActiveConn.getRealConnection(), this);
                            log.info("Claimed overdue connection " + conn.getRealHashCode() + ".");
                        } else {
                            try {
                                // 1. 2. 2 未超过，先将等待次数加一，再进行等待
                                if(!countedWait) {
                                    poolState.hadToWaitCount++;
                                    countedWait = true;
                                }

                                log.info("Waiting as long as " + poolTimeToWait + " milliseconds for " + "connection.");
                                long tm = System.currentTimeMillis();
                                poolState.wait(poolTimeToWait);
                                poolState.accumulatedWaitTime += System.currentTimeMillis() - tm;
                            } catch(InterruptedException e) {
                                break;
                            }
                        }
                    }
                }

                // 巧妙的是，等待之后不赋值，而是直接走下一次while
                // 1. 3 校验连接
                if(conn != null) {
                    // 1. 3. 1 通过, poolState 赋值
                    if(conn.isValid()) {
                        if(!conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().rollback();
                        }

                        // 此时这个conn被切换上去进行使用，更新切换时间
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));

                        poolState.requestCount++;
                        poolState.activeConnections.add(conn);
                        poolState.accumulatedRequestTime += System.currentTimeMillis() - now;
                    } else {
                        // 1. 3. 2 不通过 poolState 赋值 失败次数累加，校验不通过则抛异常
                        log.info("A bad connection (" + conn.getRealHashCode() + ") was " + "returned from the pool, getting another connection.");

                        poolState.badConnectionCount++;
                        localBadConnectionCount++;

                        conn = null;
                        if(localBadConnectionCount > (poolMaximumIdleConnections + 3)) {
                            log.debug("PooledDataSource: Could not get a good connection to " + "the database.");
                            throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
                        }
                    }
                }
            }


        }
        if(conn == null) {
            log.debug("PooledDataSource: Unknown severe error condition.  The connection pool " + "returned a null connection.");
            throw new SQLException("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");

        }

        return conn;
    }

    /**
     * 关闭所有连接
     */
    public void forceCloseAll() {
        synchronized(poolState) {

            // 更新连接编码
            expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());

            // 关闭活跃的连接
            try {
                for(int i = poolState.activeConnections.size() - 1; i >= 0; i--) {
                    PooledConnection closeConn = poolState.activeConnections.remove(i);
                    // 逻辑关闭
                    closeConn.invalidate();

                    Connection realConn = closeConn.getRealConnection();
                    if(!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }

                    realConn.close();
                }
            } catch(SQLException e) {

            }

            // 关闭空闲的连接
            try {
                for(int i = poolState.idleConnections.size() - 1; i >= 0; i--) {
                    PooledConnection closeConn = poolState.idleConnections.remove(i);
                    // 逻辑关闭
                    closeConn.invalidate();

                    Connection realConn = closeConn.getRealConnection();
                    if(!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                }
            } catch(SQLException e) {

            }

            log.info("PooledDataSource forcefully closed/removed all connections.");
        }
    }

    public void setDriver(String driver) {
        dataSource.setDriver(driver);
        forceCloseAll();
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
        forceCloseAll();
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
        forceCloseAll();
    }

    public void setUrl(String url) {
        dataSource.setUrl(url);
        forceCloseAll();
    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        DriverManager.setLogWriter(logWriter);
    }

    @Override
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }


    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        dataSource.setAutoCommit(defaultAutoCommit);
        forceCloseAll();
    }


}
