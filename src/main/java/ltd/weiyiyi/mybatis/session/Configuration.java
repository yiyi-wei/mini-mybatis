package ltd.weiyiyi.mybatis.session;

import ltd.weiyiyi.mybatis.binding.MapperRegistry;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wei Han
 * @description config content
 * @date 2024/2/17 11:47
 * @domain www.weiyiyi.ltd
 */
public class Configuration {

    /**
     * registry mapper and generate proxy factory by dao type.
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * id & mappedStatement map
     * id: dao namespace + sql id(method name in dao)
     * mappedStatement: xml node in mapper.xml
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public void addMapper(Class<?> type) {
        mapperRegistry.addMapper(type);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(String sqlId, MappedStatement mappedStatement) {
        mappedStatements.put(sqlId, mappedStatement);
    }

    public MappedStatement getMappedStatement(String sqlId) {
        return mappedStatements.get(sqlId);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }
}
