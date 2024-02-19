package ltd.weiyiyi.test.dao.dao;

/**
 * @author Wei Han
 * @description mock user myBatis mapper
 * @date 2024/2/15 23:39
 * @domain www.weiyiyi.ltd
 */
public interface IUserDao {

    String queryUserByUId(Long uid);
    String queryUserByName(String name);
}
