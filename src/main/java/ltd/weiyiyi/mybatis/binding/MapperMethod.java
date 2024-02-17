package ltd.weiyiyi.mybatis.binding;

import lombok.Data;
import ltd.weiyiyi.mybatis.mapping.MappedStatement;
import ltd.weiyiyi.mybatis.mapping.SqlCommandType;
import ltd.weiyiyi.mybatis.session.Configuration;
import ltd.weiyiyi.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/17 13:34
 * @domain www.weiyiyi.ltd
 */
public class MapperMethod {

    private final SqlCommand command;

    public <T> MapperMethod(Class<T> type, Method method, Configuration configuration) {
        command = new SqlCommand(configuration, type, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch(command.getSqlCommandType()) {
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getMethodName(), args);
                break;
            default:
                throw new RuntimeException("Unknown execution method for" + command.getMethodName());
        }
        return result;
    }

    @Data
    public static class SqlCommand {
        private String methodName;
        private SqlCommandType sqlCommandType;

        public SqlCommand(Configuration configuration, Class<?> type, Method method) {
            String sqlId = type.getName() + "." + method.getName();
            MappedStatement ms = configuration.getMappedStatement(sqlId);
            methodName = ms.getId();
            sqlCommandType = ms.getSqlCommandType();
        }
    }
}
