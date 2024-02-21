package ltd.weiyiyi.mybatis.executor;

import lombok.extern.slf4j.Slf4j;
import ltd.weiyiyi.mybatis.mapping.BoundSql;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.ResultHandler;
import ltd.weiyiyi.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 22:05
 * @domain www.weiyiyi.ltd
 */
@Slf4j
public abstract class BaseExecutor implements Executor {

    protected Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, ResultHandler resultHandler, Object param, BoundSql boundSql) {
        if(closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doQuery(ms, resultHandler, param, boundSql);
    }

    protected abstract <E> List<E> doQuery(MappedStatement ms, ResultHandler resultHandler, Object param,
                           BoundSql boundSql);

    @Override
    public void commit(boolean required) throws SQLException {
        if(closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed");
        }

        if(required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if(closed) {
            return;
        }

        if(required) {
            transaction.rollback();
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            log.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }

    @Override
    public Transaction getTransaction() {
        if(closed) {
            throw new RuntimeException("Executor was closed.");
        }

        return transaction;
    }
}
