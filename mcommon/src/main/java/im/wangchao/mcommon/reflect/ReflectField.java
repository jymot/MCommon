package im.wangchao.mcommon.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <p>Description  : Field.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 17/5/9.</p>
 * <p>Time         : 下午5:50.</p>
 */
public class ReflectField {
    private Field mField;

    private ReflectField(Field field){
        this.mField = field;
    }

    public static ReflectField create(Field field){
        return new ReflectField(field);
    }

    public void set(Object target, Object value){
        if (mField == null){
            return;
        }

        if (target == null){
            if ((mField.getModifiers() & Modifier.STATIC) == 0){
                throw new RuntimeException("Target can not be null.");
            }
        }

        try {
            mField.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object get(Object target) {
        if (mField == null)
            return null;
        if (target == null) {
            if ((mField.getModifiers() & Modifier.STATIC) == 0)
                throw new RuntimeException("Target can not be null!");
        }
        try {
            return mField.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
