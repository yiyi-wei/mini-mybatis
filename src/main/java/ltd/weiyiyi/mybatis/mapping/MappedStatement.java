package ltd.weiyiyi.mybatis.mapping;

import lombok.Data;
import ltd.weiyiyi.mybatis.session.Configuration;

import java.util.Map;

/**
 * @author Wei Han
 * @description xml node in mapper.xml
 * @date 2024/2/17 11:56
 * @domain www.weiyiyi.ltd
 */
@Data
public class MappedStatement {

    /**
     * content
     */
    private Configuration configuration;

    /**
     * method name
     */
    private String id;

    private String parameterType;
    private SqlCommandType sqlCommandType;
    private String resultType;
    private String sql;
    private Map<Integer, String> parameter;

    /**
     * disable constructor
     */
    MappedStatement() {

    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, String parameterType,
                       SqlCommandType sqlCommandType, String resultType, String sql, Map<Integer, String> parameter) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.parameterType = parameterType;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameter = parameter;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

    }

}
