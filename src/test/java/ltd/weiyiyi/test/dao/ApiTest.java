package ltd.weiyiyi.test.dao;

import ltd.weiyiyi.mybatis.binding.MapperProxyFactory;
import ltd.weiyiyi.test.dao.dao.IUserDao;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wei Han
 * @description
 * @date 2024.2.15 23:41
 * @domain www.weiyiyi.ltd
 */

public class ApiTest {

    @Test
    public void test_queryUserByUId() {
        
        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);
        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("ltd.weiyiyi.test.dao.dao.IUserDao.queryUserByUId", "模拟执行 Mapper.xml 中 SQL" +
                " 语句的操作：通过Uid查询用户");
        sqlSession.put("ltd.weiyiyi.test.dao.dao.IUserDao.queryUserByName", "mock invoke sql that" +
                " query user by name in Mapper.xml");
        IUserDao userDao = factory.newInstance(sqlSession);
        String s1 = userDao.queryUserByUId("weiyiyi");
        String s2 = userDao.queryUserByName("wei han");
        System.out.println(s1);
        System.out.println(s2);
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
