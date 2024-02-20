package ltd.weiyiyi.mybatis.dataSource.pooled;

import ltd.weiyiyi.mybatis.dataSource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 11:30
 * @domain www.weiyiyi.ltd
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    @Override
    public DataSource getDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver(properties.getProperty("driver"));
        pooledDataSource.setUsername(properties.getProperty("user"));
        pooledDataSource.setPassword(properties.getProperty("password"));
        pooledDataSource.setUrl(properties.getProperty("url"));
        return pooledDataSource;
    }
}
