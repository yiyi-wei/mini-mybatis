package ltd.weiyiyi.mybatis.dataSource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 11:55
 * @domain www.weiyiyi.ltd
 */
public class PooledConnection implements InvocationHandler {

    private static final String CLOSE = "close";

    /**
     * 代理要提供的接口
     */
    private static final Class<?>[] INTERFACES = new Class<?>[]{Connection.class};

    /**
     * 每个连接的标识
     */
    private int hashCode = 0;

    /**
     * 连的是谁
     */
    private PooledDataSource dataSource;

    /**
     * 真实的连接
     */
    private Connection realConnection;

    /**
     * 代理的连接
     */
    private Connection proxyConnection;

    private long checkoutTimestamp;

    /**
     * 创建时间戳
     */
    private long createdTimestamp;

    /**
     * 上一次操作的时间戳
     */
    private long lastUsedTimestamp;

    /**
     * 连接的标识码：("" + url + username + password).hashCode();
     */
    private int connectionTypeCode;

    /**
     * 校验位
     */
    private boolean valid;

    public PooledConnection(Connection connection, PooledDataSource dataSource) {
        this.hashCode = connection.hashCode();
        this.realConnection = connection;
        this.dataSource = dataSource;
        this.createdTimestamp = System.currentTimeMillis();
        this.lastUsedTimestamp = System.currentTimeMillis();
        this.valid = true;
        this.proxyConnection =
                (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), INTERFACES, this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        // 关闭连接，不真的关闭，而是返回池子
        if(methodName.equals(CLOSE) && methodName.hashCode() == CLOSE.hashCode()) {
            dataSource.pushConnection(this);
            return null;
        }

        if(!method.getDeclaringClass().equals(Object.class)) {
            checkConnection();
        }

        return method.invoke(realConnection, args);
    }

    private void checkConnection() throws SQLException {
        if (!valid) {
            throw new SQLException("Error accessing PooledConnection. Connection is invalid.");
        }
    }


    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public PooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public void setProxyConnection(Connection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public long getCheckoutTimestamp() {
        return checkoutTimestamp;
    }
    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimestamp;
    }

    public void setCheckoutTimestamp(long checkoutTimestamp) {
        this.checkoutTimestamp = checkoutTimestamp;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    public int getConnectionTypeCode() {
        return connectionTypeCode;
    }

    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    public void invalidate() {
        valid = false;
    }

    public boolean isValid() {
        return valid && realConnection != null && dataSource.pingConnection(this);
    }

    public int getRealHashCode() {
        return realConnection == null ? 0 : realConnection.hashCode();
    }

    public long getTimeElapsedSinceLastUse() {
        return System.currentTimeMillis() - lastUsedTimestamp;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PooledConnection) {
            return realConnection.hashCode() == (((PooledConnection) obj).realConnection.hashCode());
        } else if (obj instanceof Connection) {
            return hashCode == obj.hashCode();
        } else {
            return false;
        }
    }
}
