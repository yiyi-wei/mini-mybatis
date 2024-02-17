package ltd.weiyiyi.mybatis.builder;


import ltd.weiyiyi.mybatis.session.Configuration;

/**
 * @author Wei Han
 * @description Builder super class
 * @date 2024/2/17 11:46
 * @domain www.weiyiyi.ltd
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
