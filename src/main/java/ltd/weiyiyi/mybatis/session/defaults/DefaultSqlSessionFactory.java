package ltd.weiyiyi.mybatis.session.defaults;

import ltd.weiyiyi.mybatis.executor.Executor;
import ltd.weiyiyi.mybatis.mapping.Environment;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.SqlSession;
import ltd.weiyiyi.mybatis.session.SqlSessionFactory;
import ltd.weiyiyi.mybatis.session.TransactionIsolationLevel;
import ltd.weiyiyi.mybatis.transaction.Transaction;
import ltd.weiyiyi.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * @author Wei Han
 * @description sqlsession factory
 * @date 2024/2/16 16:25
 * @domain www.weiyiyi.ltd
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            // 1. 获取一个事务连接
            final Environment env = configuration.getEnvironment();

            TransactionFactory txFactory = env.getTransactionFactory();
            tx = txFactory.newTransaction(env.getDataSource(),
                    TransactionIsolationLevel.READ_COMMITTED, false);

            // 2. 获得执行器
            final Executor executor = configuration.newExecutor(tx);

            // 3. 用执行器 new Session
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
