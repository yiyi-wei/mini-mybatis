package ltd.weiyiyi.mybatis.builder.xml;

import ltd.weiyiyi.mybatis.builder.BaseBuilder;
import ltd.weiyiyi.mybatis.dataSource.DataSourceFactory;
import ltd.weiyiyi.mybatis.io.Resources;
import ltd.weiyiyi.mybatis.mapping.BoundSql;
import ltd.weiyiyi.mybatis.mapping.Environment;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.mapping.SqlCommandType;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/17 12:16
 * @domain www.weiyiyi.ltd
 */
public class XmlConfigBuilder extends BaseBuilder {
    private Element root;

    public XmlConfigBuilder(Reader reader) {
        super(new Configuration());

        // dom4j
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch(DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration parse() {
        try {
            environmentsElement(root.element("environments"));

            mapperElement(root.element("mappers"));
        } catch(Exception e) {
            throw new RuntimeException("Error parsing SQL mapper configuration. Cause: " + e, e);
        }
        return configuration;
    }

    /*
     * <environments default="development">
     *      <environment id="development">
     *          <transactionManager type="JDBC">
     *              <property name="..." value="..."/>
     *          </transactionManager>
     *          <dataSource type="POOLED">
     *              <property name="driver" value="${driver}"/>
     *              <property name="url" value="${url}"/>
     *              <property name="username" value="${username}"/>
     *              <property name="password" value="${password}"/>
     *          </dataSource>
     *      </environment>
     * </environments>
     */
    private void environmentsElement(Element environments) throws Exception {
        String defaultEnvironment = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");

        for(Element environment : environmentList) {
            String envId = environment.attributeValue("id");
            if(!defaultEnvironment.equals(envId)) {
                continue;
            }

            String txType = environment.element("transactionManager").attributeValue("type");

            Element dataSourceElement = environment.element("dataSource");
            String dataSourceType = dataSourceElement.attributeValue("type");
            List<Element> propertyList = dataSourceElement.elements("property");

            DataSourceFactory dsFactory =
                    (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceType)
                    .newInstance();

            Properties props = new Properties();
            for(Element property : propertyList) {
                props.put(property.attributeValue("name"), property.attributeValue("value"));
            }
            dsFactory.setProperties(props);

            DataSource dataSource = dsFactory.getDataSource();
            TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(txType)
                    .newInstance();
            configuration.setEnvironment(new Environment(envId, txFactory, dataSource));
        }
    }

    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for(Element e : mapperList) {
            String resource = e.attributeValue("resource");
            Reader reader = Resources.getSourceAsReader(resource);

            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element rootElement = document.getRootElement();

            // dao class path
            String namespace = rootElement.attributeValue("namespace");

            List<Element> elementList = rootElement.elements("select");

            // select xml nodes
            for(Element node : elementList) {
                String id = node.attributeValue("id");
                String sqlId = namespace + "." + id;
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();
                String nodeName = node.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

                Map<Integer, String> parameters = new HashMap<>();
                // parse parameter by regex
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for(int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameters.put(i, g2);
                    sql = sql.replace(g1, "?");
                }

                BoundSql boundSql = new BoundSql(sql, parameterType, parameters, resultType);

                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, sqlId, sqlCommandType, boundSql).build();

                configuration.addMappedStatement(sqlId, mappedStatement);
            }
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

}
