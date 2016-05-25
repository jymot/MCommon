package im.wangchao.mcommon.utils;

import android.support.annotation.NonNull;

import java.lang.reflect.Modifier;

/**
 * <p>Description  : ReflectUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/26.</p>
 * <p>Time         : 下午1:47.</p>
 */
public class ReflectUtils {
    private ReflectUtils(){
        throw new AssertionError();
    }

    /**
     * Learn whether the specified class is generally accessible, i.e. is
     * declared in an entirely {@code public} manner.
     * @param type to check
     * @return {@code true} if {@code type} and any enclosing classes are
     *         {@code public}.
     */
    public static boolean isAccessible(@NonNull final Class<?> type) {
        Class<?> cls = type;
        while (cls != null) {
            if (!Modifier.isPublic(cls.getModifiers())) {
                return false;
            }
            cls = cls.getEnclosingClass();
        }
        return true;
    }
}
