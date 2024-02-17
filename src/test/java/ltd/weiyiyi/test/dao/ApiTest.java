package ltd.weiyiyi.test.dao;

import ltd.weiyiyi.mybatis.io.Resources;
import ltd.weiyiyi.mybatis.session.SqlSession;
import ltd.weiyiyi.mybatis.session.SqlSessionFactory;
import ltd.weiyiyi.mybatis.session.SqlSessionFactoryBuilder;
import ltd.weiyiyi.test.dao.dao.IUserDao;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Proxy;

/**
 * @author Wei Han
 * @description
 * @date 2024.2.15 23:41
 * @domain www.weiyiyi.ltd
 */

public class ApiTest {

    @Test
    public void test_queryUserByUId() throws IOException {

        Reader sourceAsReader = Resources.getSourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(sourceAsReader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        String id = userDao.queryUserByUId("wei han");

        System.out.println(id);

    }

    @Test
    public void test_proxy_class() {
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class}, (proxy, method, args) -> "你被代理了！");
        String result = userDao.queryUserByUId("weiyiyi");
        System.out.println("result：" + result);
    }

}
