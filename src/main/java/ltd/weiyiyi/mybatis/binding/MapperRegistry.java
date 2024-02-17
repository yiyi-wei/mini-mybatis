package ltd.weiyiyi.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Wei Han
 * @description supply to add / get mapper
 * @date 2024/2/16 16:26
 * @domain www.weiyiyi.ltd
 */
public class MapperRegistry {

    final Map<Class<?>, MapperProxyFactory<?>> knownMapper = new HashMap();

    private Configuration config;

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {

        MapperProxyFactory<?> mapperProxyFactory = knownMapper.get(type);
        if(mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not exist in the mapperRegistry.");
        }

        try {
            return (T) mapperProxyFactory.newInstance(sqlSession);
        } catch(Exception e) {
            throw new RuntimeException("Error to get instance mapper of class[" + type + "]. " +
                    "Cause: " + e, e);
        }
    }

    public void addMapper(Class<?> type) {
        if(!type.isInterface()) {
            return;
        }

        // exist in mapper error
        if(hasMapper(type)) {
            throw new RuntimeException("type " + type + " is already known in the mapperRegistry.");
        }

        MapperProxyFactory<?> mapperProxyFactory = new MapperProxyFactory<>(type);

        knownMapper.put(type, mapperProxyFactory);
    }

    public void addMappers(String packageName) {
        Set<Class<?>> classes = ClassScanner.scanPackage(packageName);
        for(Class<?> clazz : classes) {
            addMapper(clazz);
        }
    }

    public boolean hasMapper(Class<?> type) {
        return knownMapper.containsKey(type);
    }
}
