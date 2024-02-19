package ltd.weiyiyi.mybatis.dataSource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import ltd.weiyiyi.mybatis.dataSource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/19 15:10
 * @domain www.weiyiyi.ltd
 */
public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public DataSource getDataSource() {
        /*   <property name="driver" value="${driver}"/>
         *   <property name="url" value="${url}"/>
         *   <property name="username" value="${username}"/>
         *   <property name="password" value="${password}"/>
         */
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName(props.getProperty("driver"));
        dataSource.setUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));

        return dataSource;
    }
}
