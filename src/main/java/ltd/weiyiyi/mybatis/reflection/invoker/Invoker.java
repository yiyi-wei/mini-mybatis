package ltd.weiyiyi.mybatis.reflection.invoker;

/**
 * @author Wei Han
 * @description 反射拦截调用类
 * @date 2024/2/22 12:09
 * @domain www.weiyiyi.ltd
 */
public interface Invoker {

    /**
     * 拦截执行
     *
     * @param target 调用方法的对象就是x：x.method 即：被代理对象
     * @param args   args
     * @return returned
     */
    Object invoke(Object target, Object[] args) throws Exception;

    /**
     * 返回方法调用者的类型
     *
     * @return class type
     */
    Class<?> getType();
}
