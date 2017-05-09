package im.wangchao.mcommon.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>Description  : Constructor.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 17/5/9.</p>
 * <p>Time         : 下午5:50.</p>
 */
public class ReflectConstructor {
    private Constructor mConstructor;

    private ReflectConstructor(Constructor constructor){
        this.mConstructor = constructor;
    }

    public static ReflectConstructor create(Constructor constructor){
        return new ReflectConstructor(constructor);
    }

    public Object newInstance(Object ...args){
        Object target = null;
        if (mConstructor != null){
            try {
                target = mConstructor.newInstance(args);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return target;
    }
}
