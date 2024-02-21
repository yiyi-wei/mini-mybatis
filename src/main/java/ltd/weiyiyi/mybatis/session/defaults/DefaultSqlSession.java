package ltd.weiyiyi.mybatis.session.defaults;

import ltd.weiyiyi.mybatis.executor.Executor;
import ltd.weiyiyi.mybatis.mapping.Environment;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.SqlSession;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/16 17:06
 * @domain www.weiyiyi.ltd
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + "方法：" + statement);

    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        Environment environment = configuration.getEnvironment();
        DataSource dataSource = environment.getDataSource();
        List<Object> objectList = executor.query(mappedStatement, Executor.NO_RESULT_HANDLER, parameter, mappedStatement.getBoundSql());

        return (T) objectList.get(0);
    }



    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    /**
     * get content config
     *
     * @return configuration
     */
    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
