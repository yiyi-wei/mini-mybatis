package ltd.weiyiyi.test.dao;

import ltd.weiyiyi.mybatis.dataSource.pooled.PooledDataSource;
import ltd.weiyiyi.mybatis.io.Resources;
import ltd.weiyiyi.mybatis.session.SqlSession;
import ltd.weiyiyi.mybatis.session.SqlSessionFactory;
import ltd.weiyiyi.mybatis.session.SqlSessionFactoryBuilder;
import ltd.weiyiyi.test.dao.dao.IUserDao;
import ltd.weiyiyi.test.dao.po.User;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Wei Han
 * @description
 * @date 2024.2.15 23:41
 * @domain www.weiyiyi.ltd
 */

public class ApiTest {

    @Test
    public void test_queryUserByUId() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getSourceAsReader(
                "mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User user = userDao.queryUserByUId(1L);
        //for (int i = 0; i < 50; i++) {
        //    System.out.println(JSON.toJSONString(user));
        //}
        System.out.println(user);
    }

    @Test
    public void test_proxy_class() {
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class}, (proxy, method, args) -> "你被代理了！");
        User result = userDao.queryUserByUId(10001L);
        System.out.println("result：" + result);
    }

    @Test
    public void test_pooled() throws SQLException, InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver("com.mysql.cj.jdbc.Driver");
        pooledDataSource.setUrl("jdbc:mysql://10.211.55.12:3306/mybatis?useUnicode=true");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("Weihan1234");
        // 持续获得链接
        while (true){
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            Thread.sleep(1000);
            //connection.close();
        }
    }
}
