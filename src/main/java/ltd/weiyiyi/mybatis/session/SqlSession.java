package ltd.weiyiyi.mybatis.session;

/**
 * @author Wei Han
 * @description analyze and invoke sql in mapper.xml
 * @date 2024/2/16 16:44
 * @domain www.weiyiyi.ltd
 */
public interface SqlSession {

    /**
     * invoke a sql that query a row in db
     *
     * @param <T>       type of boxing obj
     * @param statement sqlId(method name in mapper interface)
     * @return boxing obj
     */
    <T> T selectOne(String statement);

    /**
     * invoke a sql that query a row with condition in db
     *
     * @param <T>       type of boxing obj
     * @param statement sqlId(method name in mapper interface)
     * @param parameter obj or map ...
     * @return boxing obj
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * mapper interface of invoke sql id
     *
     * @param <T>  mapper type
     * @param type T type
     * @return mapper
     */
    <T> T getMapper(Class<T> type);
}
