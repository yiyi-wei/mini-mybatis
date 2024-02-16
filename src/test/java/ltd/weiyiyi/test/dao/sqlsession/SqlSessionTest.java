package ltd.weiyiyi.test.dao.sqlsession;

import ltd.weiyiyi.mybatis.binding.MapperRegistry;
import ltd.weiyiyi.mybatis.session.SqlSession;
import ltd.weiyiyi.mybatis.session.defaults.DefaultSqlSessionFactory;
import ltd.weiyiyi.test.dao.dao.IUserDao;
import org.junit.Test;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/16 17:11
 * @domain www.weiyiyi.ltd
 */
public class SqlSessionTest {

    @Test
    public void test_sqlSession() {
        // 1. registry mappers by package name
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("ltd.weiyiyi.test.dao.dao");

        // 2. get sql session by factory
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = defaultSqlSessionFactory.openSession();

        // 3. get instance
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4. proxy invoke method
        String res = userDao.queryUserByUId("wei han");
        System.out.println(res);

    }
}
