package im.wangchao.mcommon.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * <p>Description  : Method.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 17/5/9.</p>
 * <p>Time         : 下午5:50.</p>
 */
public class ReflectMethod {
    private Method mMethod;

    private ReflectMethod(Method method){
        this.mMethod = method;
    }

    public static ReflectMethod create(Method method){
        return new ReflectMethod(method);
    }

    public Object invoke(Object invoker, Object... params) {
        if (mMethod == null)
            return null;
        if (invoker == null) {
            if ((mMethod.getModifiers() & Modifier.STATIC) == 0)
                throw new RuntimeException("Invoker can not be null!");
        }
        try {
            if (params == null || params.length == 0)
                return mMethod.invoke(invoker);
            return mMethod.invoke(invoker, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
