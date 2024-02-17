package ltd.weiyiyi.mybatis.session;

/**
 * @author Wei Han
 * @description SqlSession 用来执行SQL，获取映射器，管理事务。
 * PS：通常情况下，我们在应用程序中使用的Mybatis的API就是这个接口定义的方法。
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

    /**
     * get content config
     *
     * @return configuration
     */
    Configuration getConfiguration();
}
