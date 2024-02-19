package ltd.weiyiyi.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Wei Han
 * @description transaction
 * @date 2024/2/19 14:23
 * @domain www.weiyiyi.ltd
 */
public interface Transaction {

    Connection getConnection() throws SQLException;
    void commit() throws SQLException;
    
    void rollback() throws SQLException;
    void close() throws SQLException;
}
