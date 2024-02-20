package ltd.weiyiyi.mybatis.session;

import ltd.weiyiyi.mybatis.binding.MapperRegistry;
import ltd.weiyiyi.mybatis.dataSource.druid.DruidDataSourceFactory;
import ltd.weiyiyi.mybatis.dataSource.pooled.PooledDataSourceFactory;
import ltd.weiyiyi.mybatis.dataSource.unpooled.UnpooledDataSourceFactory;
import ltd.weiyiyi.mybatis.mapping.Environment;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.transaction.jdbc.JdbcTransactionFactory;
import ltd.weiyiyi.mybatis.type.TypeAliasRegistry;

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
     * env
     */
    protected Environment environment;

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

    /**
     * register alias for type
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

    }
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

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }
}
