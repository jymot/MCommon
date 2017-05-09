package im.wangchao.mcommon.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static im.wangchao.mcommon.utils.Utils.checkNotNull;

/**
 * <p>Description  : Reflect.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 17/5/9.</p>
 * <p>Time         : 下午5:28.</p>
 */
public class Reflect {
    private Class<?> targetCls;

    private Reflect(String clsName){
        checkNotNull(clsName);
        try {
            this.targetCls = Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class: " + clsName + " is not found.", e.getCause());
        }
    }

    private Reflect(Class<?> cls){
        checkNotNull(cls);
        this.targetCls = cls;
    }

    public static Reflect with(String clsName){
       return new Reflect(clsName);
    }

    public static Reflect with(Class<?> cls){
        return new Reflect(cls);
    }

    public ReflectConstructor constructor(Class<?>... parameterTypes){
        Constructor constructor = null;
        try {
            constructor = targetCls.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return ReflectConstructor.create(constructor);
    }

    public ReflectField field(String fieldName){
        Field field = null;
        try {
            field = targetCls.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return ReflectField.create(field);
    }

    public ReflectMethod method(String methodName, Class<?>... types) {
        Method method = null;
        try {
            method = targetCls.getDeclaredMethod(methodName, types);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return ReflectMethod.create(method);
    }

}
