package ltd.weiyiyi.mybatis.reflection.invoker;

import java.lang.reflect.Method;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/22 12:13
 * @domain www.weiyiyi.ltd
 */
public class MethodInvoker implements Invoker {

    private Method method;
    private Class<?> type;

    public MethodInvoker(Method method) {
        this.method = method;
        if(method.getParameterCount() == 1) {
            type = method.getParameterTypes()[0];
        } else {
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return method.invoke(target, args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
