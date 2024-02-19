package ltd.weiyiyi.mybatis.session;

import ltd.weiyiyi.mybatis.builder.xml.XmlConfigBuilder;
import ltd.weiyiyi.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;


/**
 * @author Wei Han
 * @description mybatis enter class to build SqlSessionFactory
 * @date 2024/2/17 13:13
 * @domain www.weiyiyi.ltd
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        // parse config.xml to build configuration
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        Configuration configuration = xmlConfigBuilder.parse();
        return build(configuration);
    }

    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}
