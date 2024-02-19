package ltd.weiyiyi.mybatis.builder;


import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.type.TypeAliasRegistry;

/**
 * @author Wei Han
 * @description Builder super class
 * @date 2024/2/17 11:46
 * @domain www.weiyiyi.ltd
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    protected BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
