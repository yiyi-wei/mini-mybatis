package ltd.weiyiyi.mybatis.dataSource.unpooled;

import lombok.Data;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/19 21:10
 * @domain www.weiyiyi.ltd
 */
@Data
public class UnpooledDataSource implements DataSource {

    private ClassLoader driverClassLoader;

    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();

    /**
     * other props'  driver.Encoding=UTF8
     */
    private Properties driverProperties;

    /**
     * driver str: com.mysql.cj.jdbc.Driver
     */
    private String driver;

    /**
     * db url
     */
    private String url;

    private String username;

    private String password;

    private Boolean autoCommit;

    /**
     * tx level
     */
    private Integer txIsolationLevel;

    /**
     * init driver in system
     */
    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements()) {
            Driver currDriver = drivers.nextElement();
            registeredDrivers.put(currDriver.getClass().getName(), currDriver);
        }
    }

    private static class DriverProxy implements Driver {

        private Driver driver;

        public DriverProxy(Driver driver) {
            this.driver = driver;
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }

    private Connection doGetConnection(Properties properties) throws SQLException {
        initDriver();
        Connection connection = DriverManager.getConnection(url, properties);

        if(autoCommit != null && autoCommit != connection.getAutoCommit()) {
            connection.setAutoCommit(autoCommit);
        }

        if(txIsolationLevel != null) {
            connection.setTransactionIsolation(txIsolationLevel);
        }

        return connection;
    }

    private Connection doGetConnection(String username, String password) throws SQLException {
        Properties properties = new Properties();
        if(driverProperties != null) {
            properties.putAll(driverProperties);
        }

        if(password != null) {
            properties.put("password", password);
        }

        if(username != null) {
            properties.put("user", username);
        }
        return doGetConnection(properties);
    }

    private synchronized void initDriver() throws SQLException {
        if(registeredDrivers.containsKey(driver)) {
            return;
        }

        try {
            Class<?> driverClass = Class.forName(driver, true, driverClassLoader);
            Driver driverInstance = (Driver) driverClass.newInstance();
            DriverManager.registerDriver(new DriverProxy(driverInstance));
            registeredDrivers.put(driver, driverInstance);
        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException |
                SQLException e) {
            throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection(username, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }



}
