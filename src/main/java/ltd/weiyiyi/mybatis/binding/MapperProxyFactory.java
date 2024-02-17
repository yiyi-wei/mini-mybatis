package ltd.weiyiyi.mybatis.binding;

import ltd.weiyiyi.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/15 23:25
 * @domain www.weiyiyi.ltd
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Map<Method, MapperMethod> getMapperMethod() {
        return methodCache;
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> proxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        return (T) Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
                new Class[]{mapperInterface}, proxy);
    }
}
