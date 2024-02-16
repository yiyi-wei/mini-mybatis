package ltd.weiyiyi.mybatis.binding;

import ltd.weiyiyi.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/15 23:25
 * @domain www.weiyiyi.ltd
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> proxy = new MapperProxy<T>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
                new Class[]{mapperInterface}, proxy);
    }
}
