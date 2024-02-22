package ltd.weiyiyi.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/22 13:02
 * @domain www.weiyiyi.ltd
 */
public class GetFieldInvoker implements Invoker {

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
