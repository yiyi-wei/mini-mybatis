package ltd.weiyiyi.mybatis.reflection.wrapper;

import ltd.weiyiyi.mybatis.reflection.MetaObject;

/**
 * @author Wei Han
 * @description 默认对象包装工厂
 * @date 2024/2/22 14:39
 * @domain www.weiyiyi.ltd
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory{

    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }

}

