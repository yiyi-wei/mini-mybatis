package ltd.weiyiyi.mybatis.executor;

import ltd.weiyiyi.mybatis.mapping.BoundSql;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.session.ResultHandler;
import ltd.weiyiyi.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Wei Han
 * @description 管理事务、执行相关的执行器接口
 * @date 2024/2/20 21:57
 * @domain www.weiyiyi.ltd
 */
public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, ResultHandler resultHandler, Object param,
                      BoundSql boundSql);

    void commit(boolean required) throws SQLException;
    void rollback(boolean required) throws SQLException;
    void close(boolean forceRollback);

    Transaction getTransaction();
}
