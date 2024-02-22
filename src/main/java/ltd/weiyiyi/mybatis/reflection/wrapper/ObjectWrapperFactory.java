package ltd.weiyiyi.mybatis.reflection.wrapper;

import ltd.weiyiyi.mybatis.reflection.MetaObject;

/**
 * @author Wei Han
 * @description 对象包装工厂
 * @date 2024/2/22 14:37
 * @domain www.weiyiyi.ltd
 */
public interface ObjectWrapperFactory {

    /**
     * 判断有没有包装器
     */
    boolean hasWrapperFor(Object object);

    /**
     * 得到包装器
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}

