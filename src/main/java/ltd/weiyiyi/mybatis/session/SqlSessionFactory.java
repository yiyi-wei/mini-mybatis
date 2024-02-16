package ltd.weiyiyi.mybatis.session;

/**
 * @author Wei Han
 * @description generate sql session
 * @date 2024/2/16 16:25
 * @domain www.weiyiyi.ltd
 */
public interface SqlSessionFactory {

    /**
     * open a sql session
     * @return sqlSession instance
     */
    SqlSession openSession();
}
