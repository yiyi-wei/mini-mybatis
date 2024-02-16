package ltd.weiyiyi.mybatis.session.defaults;

import ltd.weiyiyi.mybatis.binding.MapperRegistry;
import ltd.weiyiyi.mybatis.session.SqlSession;
import ltd.weiyiyi.mybatis.session.SqlSessionFactory;

/**
 * @author Wei Han
 * @description sqlsession factory
 * @date 2024/2/16 16:25
 * @domain www.weiyiyi.ltd
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
