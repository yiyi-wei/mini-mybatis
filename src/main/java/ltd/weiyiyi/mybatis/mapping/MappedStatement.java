package ltd.weiyiyi.mybatis.mapping;

import lombok.Data;
import ltd.weiyiyi.mybatis.session.Configuration;

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

    private SqlCommandType sqlCommandType;

    private BoundSql boundSql;

    /**
     * disable constructor
     */
    MappedStatement() {

    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id,
                       SqlCommandType sqlCommandType, BoundSql bound) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = bound;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

    }

}
