package im.wangchao.mcommon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>Description  : SharedPreferencesUtils.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/9.</p>
 * <p>Time         : 下午8:46.</p>
 */
public class SharedPreferencesUtils {
    private static volatile SharedPreferences sp;
    private static final    Method apply = getApplyMethod();
    private SharedPreferencesUtils(){
        throw new AssertionError();
    }

    /**
     * Save key - value
     *
     * @param context   Context
     * @param key       Key
     * @param value     Value
     */
    public static void put(@NonNull Context context, @NonNull String key, Object value){
        final SharedPreferences sp = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sp.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }

        apply(editor);
    }

    /**
     * Get value with key
     *
     * @param context   Context
     * @param key       Key
     * @param <T>       Target value
     * @return          (T)value, may be null
     */
    @Nullable public static <T> T get(@NonNull Context context, @NonNull String key){
        return opt(context, key, null);
    }

    /**
     * Opt value with key
     *
     * @param context       Context
     * @param key           Key
     * @param defaultValue  Value to return if this preference does not exist.
     * @param <T>           Target value
     * @return              (T)value, may be null
     */
    @Nullable @SuppressWarnings({"unchecked"})public static <T> T opt(@NonNull Context context, @NonNull String key, T defaultValue){
        final SharedPreferences sp = getSharedPreferences(context);
        final Map<String, ?> map = sp.getAll();
        T t;
        try {
            t = (T) map.get(key);
            if (t == null){
                t = defaultValue;
            }
        } catch (Exception e){
            t = defaultValue;
        }

        return t;
    }


    /**
     * remove key
     */
    public static void remove(@NonNull Context context, @NonNull String key) {
        final SharedPreferences sp = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        apply(editor);
    }

    /**
     * clear editor
     */
    public static void clear(@NonNull Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        apply(editor);
    }

    /**
     * contains key
     */
    public static boolean contains(@NonNull Context context, @NonNull String key) {
        final SharedPreferences sp = getSharedPreferences(context);
        return sp.contains(key);
    }

    /**
     * get all
     */
    public static Map<String, ?> getAll(@NonNull Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        return sp.getAll();
    }

    /**
     * get SharedPreferences instance
     */
    private static SharedPreferences getSharedPreferences(@NonNull Context context){
        if (sp == null){
            synchronized (SharedPreferencesUtils.class){
                if (sp == null){
                    String name = context.getPackageName().concat(SharedPreferencesUtils.class.getSimpleName());
                    sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
                }
            }
        }

        return sp;
    }

    /**
     * compat apply
     */
    private static void apply(SharedPreferences.Editor editor){
        if (apply != null){
            try {
                apply.invoke(editor);
                return;
            } catch (Exception e) {
                //Silent
            }
        }
        editor.commit();
    }

    @SuppressWarnings({"unchecked"})private static Method getApplyMethod(){
        try {
            Class clz = SharedPreferences.Editor.class;
            return clz.getMethod("apply");
        } catch (NoSuchMethodException e) {
            //Silent
        }
        return null;
    }
}
