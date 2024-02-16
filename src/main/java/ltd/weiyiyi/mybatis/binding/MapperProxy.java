package ltd.weiyiyi.mybatis.binding;

import ltd.weiyiyi.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Wei Han
 * @description mapper proxy invoke all of interface's method
 * @date 2024/2/15 23:13
 * @domain www.weiyiyi.ltd
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 6424333398559729838L;
    private SqlSession sqlSession;

    private final Class<T> mapperInterface;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Method of class object do not require proxy invoke because we do not need to worry
        // about them such as toString(), hashcode()
        if(Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            // sqlSession is a map of classAllName & Mapper.xml
            // I will run sql of mapper.xml after do something sqlSession value
            return "method proxy " + sqlSession.selectOne(method.getName(), args);
        }
    }
}
