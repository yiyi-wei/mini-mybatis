package ltd.weiyiyi.mybatis.dataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/19 15:09
 * @domain www.weiyiyi.ltd
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();
}
