package ltd.weiyiyi.mybatis.executor.statement;

import ltd.weiyiyi.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 22:24
 * @domain www.weiyiyi.ltd
 */
public interface StatementHandler {

    /**
     * 准备语句(statement)
     * statement: 用于执行静态SQL语句并返回它所生成结果的对象
     *
     * @param connection 事务conn
     * @return 语句
     */
    Statement prepare(Connection connection) throws SQLException;

    /**
     * 将?替换为参数
     *
     * @param statement 语句
     * @param parameter 参数
     */
    void parameterize(Statement statement, Object parameter) throws SQLException;

    /**
     * 执行query
     *
     * @param statement     语句
     * @param resultHandler 结果处理器
     * @param <E>           result type
     * @return result set
     */
    <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;
}
