package ltd.weiyiyi.mybatis.executor;

import ltd.weiyiyi.mybatis.executor.statement.StatementHandler;
import ltd.weiyiyi.mybatis.mapping.BoundSql;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.ResultHandler;
import ltd.weiyiyi.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 22:05
 * @domain www.weiyiyi.ltd
 */
public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, ResultHandler resultHandler, Object param, BoundSql boundSql) {

        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler statementHandler = configuration.newStatementHandler(this, ms, resultHandler, param, boundSql);
            Connection connection = transaction.getConnection();
            Statement statement = statementHandler.prepare(connection);
            statementHandler.parameterize(statement, param);
            return statementHandler.query(statement, resultHandler);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
