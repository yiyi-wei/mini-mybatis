package ltd.weiyiyi.mybatis.transaction.jdbc;

import ltd.weiyiyi.mybatis.session.TransactionIsolationLevel;
import ltd.weiyiyi.mybatis.transaction.Transaction;
import ltd.weiyiyi.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Wei Han
 * @description jdbc t f
 * @date 2024/2/19 14:23
 * @domain www.weiyiyi.ltd
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
