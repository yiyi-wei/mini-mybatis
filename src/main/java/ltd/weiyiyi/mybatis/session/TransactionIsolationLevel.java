package ltd.weiyiyi.mybatis.session;

import java.sql.Connection;

/**
 * @author Wei Han
 * @description Isolation level
 * @date 2024/2/19 14:31
 * @domain www.weiyiyi.ltd
 */
public enum TransactionIsolationLevel {

    // JDBC support
    NONE(Connection.TRANSACTION_NONE),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private final int level;

    TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}

