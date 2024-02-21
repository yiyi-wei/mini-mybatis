package ltd.weiyiyi.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 23:16
 * @domain www.weiyiyi.ltd
 */
public interface ResultSetHandler {

    /**
     * 处理sql结果，resultSet -> objList
     *
     * @param statement 语句
     * @param <E>       result type
     * @return objList
     */
    <E> List<E> handleResultSets(Statement statement) throws SQLException;
}
