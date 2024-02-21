package ltd.weiyiyi.mybatis.executor.statement;

import ltd.weiyiyi.mybatis.executor.Executor;
import ltd.weiyiyi.mybatis.executor.resultset.ResultSetHandler;
import ltd.weiyiyi.mybatis.mapping.BoundSql;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 22:32
 * @domain www.weiyiyi.ltd
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;
    protected final ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement,
                                Object parameterObject, ResultHandler resultHandler,
                                BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;

        this.parameterObject = parameterObject;
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
    }

    @Override
    public Statement prepare(Connection connection) {
        try {
            Statement statement = null;
            statement = instantiateStatement(connection);
            // 参数设置，可以被抽取，提供配置
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch(Exception e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    /**
     * 实例化statement
     *
     * @param connection conn
     * @return 根据不同的子类实现不同的statement，分别是带?传参和不带的
     * @throws SQLException jdbc的e
     */
    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}