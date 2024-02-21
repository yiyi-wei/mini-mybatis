package ltd.weiyiyi.mybatis.executor.statement;

import ltd.weiyiyi.mybatis.executor.Executor;
import ltd.weiyiyi.mybatis.mapping.BoundSql;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 22:40
 * @domain www.weiyiyi.ltd
 */
public class SimpleStatementHandler extends BaseStatementHandler {
    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterize(Statement statement, Object parameter) {
        // N/A
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        statement.execute(boundSql.getSql());
        return resultSetHandler.handleResultSets(statement);
    }
}
