package ltd.weiyiyi.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/22 12:51
 * @domain www.weiyiyi.ltd
 */
public class SetFieldInvoker implements Invoker {

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
