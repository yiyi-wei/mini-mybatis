package ltd.weiyiyi.test.dao;

import com.alibaba.fastjson.JSON;
import ltd.weiyiyi.mybatis.dataSource.pooled.PooledDataSource;
import ltd.weiyiyi.mybatis.io.Resources;
import ltd.weiyiyi.mybatis.reflection.MetaObject;
import ltd.weiyiyi.mybatis.reflection.SystemMetaObject;
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
import java.util.ArrayList;
import java.util.List;

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
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getSourceAsReader("mybatis-config-datasource.xml"));
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
    public void test_reflection() {
        //第一次读取
        Teca teca = new Teca();

        List<Teca.Stu> objects = new ArrayList<>();
        objects.add(new Teca.Stu());
        teca.setName("lili");
        teca.setStus(objects);

        MetaObject metaObject = SystemMetaObject.forObject(teca);
        System.out.println("getGetterNames：" + JSON.toJSONString(metaObject.getGetterNames()));
        System.out.println("getSetterNames：" + JSON.toJSONString(metaObject.getSetterNames()));
        System.out.println("name的get方法返回值：" + JSON.toJSONString(metaObject.getGetterType("name")));
        System.out.println("stus的set方法参数值：" + JSON.toJSONString(metaObject.getGetterType("stus")));
        System.out.println("name的hasGetter：" + metaObject.hasGetter("name"));
        // 出现：UnsupportedOperationException异常
        // System.out.println("stus.id(属性为集合)的hasGetter：" + metaObject.hasGetter("stus.id"));
        System.out.println("stu.id（属性为对象）的hasGetter：" + metaObject.hasGetter("stu.id"));
        System.out.println("获取name的属性值：" + metaObject.getValue("name"));
        // 重新设置属性值
        metaObject.setValue("name", "huahua");
        System.out.println("设置name的属性值：" + metaObject.getValue("name"));
        // 设置属性（集合）的元素值
        metaObject.setValue("stus[0].id", "001");
        System.out.println("获取stus集合的第一个元素的属性值：" + JSON.toJSONString(metaObject.getValue("stus[0].id")));
        System.out.println("对象的序列化：" + JSON.toJSONString(teca));
    }

    @Test
    public void test_proxy_class() {
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class[]{IUserDao.class}, (proxy, method, args) -> "你被代理了！");
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
        while(true) {
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            Thread.sleep(1000);
            //connection.close();
        }
    }

    static class Teca {

        private String name;

        private double price;

        private List<Stu> stus;

        private Stu stu;

        public static class Stu {

            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public List<Stu> getStus() {
            return stus;
        }

        public void setStus(List<Stu> stus) {
            this.stus = stus;
        }

        public Stu getStu() {
            return stu;
        }

        public void setStu(Stu stu) {
            this.stu = stu;
        }
    }

}

