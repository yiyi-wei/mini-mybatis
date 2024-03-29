package ltd.weiyiyi.test.dao.dao;

import ltd.weiyiyi.test.dao.po.User;

/**
 * @author Wei Han
 * @description mock user myBatis mapper
 * @date 2024/2/15 23:39
 * @domain www.weiyiyi.ltd
 */
public interface IUserDao {

    User queryUserByUId(Long uid);
    String queryUserByName(String name);
}
