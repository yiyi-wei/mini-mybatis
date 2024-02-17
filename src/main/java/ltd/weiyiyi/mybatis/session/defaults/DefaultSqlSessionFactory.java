package ltd.weiyiyi.mybatis.session.defaults;

import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.SqlSession;
import ltd.weiyiyi.mybatis.session.SqlSessionFactory;

/**
 * @author Wei Han
 * @description sqlsession factory
 * @date 2024/2/16 16:25
 * @domain www.weiyiyi.ltd
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
