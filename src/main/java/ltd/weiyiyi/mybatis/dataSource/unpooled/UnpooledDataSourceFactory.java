package ltd.weiyiyi.mybatis.dataSource.unpooled;

import ltd.weiyiyi.mybatis.dataSource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/19 21:08
 * @domain www.weiyiyi.ltd
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected Properties properties;

    @Override
    public void setProperties(Properties props) {
        this.properties = props;
    }

    @Override
    public DataSource getDataSource() {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(properties.getProperty("driver"));
        unpooledDataSource.setUsername(properties.getProperty("user"));
        unpooledDataSource.setPassword(properties.getProperty("password"));
        unpooledDataSource.setUrl(properties.getProperty("url"));
        return unpooledDataSource;
    }
}
