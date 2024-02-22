package ltd.weiyiyi.mybatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * @author Wei Han
 * @description 对象工厂接口
 * @date 2024/2/22 14:05
 * @domain www.weiyiyi.ltd
 */
public interface ObjectFactory {

    /**
     * 设置属性
     *
     * @param properties configuration properties
     */
    void setProperties(Properties properties);

    /**
     * Creates a new object with default constructor.
     * 生产对象
     *
     * @param type Object type
     * @return <T>
     */
    <T> T create(Class<T> type);

    /**
     * 生产对象，使用指定的构造函数和构造函数参数
     *
     * @param type                Object type
     * @param constructorArgTypes Constructor argument types
     * @param constructorArgs     Constructor argument values
     * @return
     */
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    /**
     * 返回这个对象是否是集合，为了支持 Scala collections
     *
     * @param type Object type
     * @return whether it is a collection or not
     * @since 3.1.0
     */
    <T> boolean isCollection(Class<T> type);

}

