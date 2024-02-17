package ltd.weiyiyi.mybatis.session.defaults;

import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.SqlSession;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/16 17:06
 * @domain www.weiyiyi.ltd
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + "方法：" + statement);

    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());

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
