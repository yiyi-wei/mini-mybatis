package ltd.weiyiyi.mybatis.transaction;

import ltd.weiyiyi.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Wei Han
 * @description transaction Factory
 * @date 2024/2/19 14:22
 * @domain www.weiyiyi.ltd
 */
public interface TransactionFactory {

    /**
     * generate transaction by connection
     *
     * @param conn Existing database connection
     * @return Transaction
     */
    Transaction newTransaction(Connection conn);

    /**
     * generate transaction by database level and autoCommit
     *
     * @param dataSource DataSource to take the connection from
     * @param level      Desired isolation level
     * @param autoCommit Desired autocommit
     * @return Transaction
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);

}
