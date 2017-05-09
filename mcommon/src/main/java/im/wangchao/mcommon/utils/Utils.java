package im.wangchao.mcommon.utils;

/**
 * <p>Description  : Utils.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 17/5/9.</p>
 * <p>Time         : 下午9:22.</p>
 */
public class Utils {
    private Utils(){
        throw new AssertionError();
    }

    public static <T> T checkNotNull(T t){
        if (t == null){
            throw new NullPointerException();
        }

        return t;
    }

    public static <T> T checkNotNull(T t, String cause){
        if (t == null){
            throw new NullPointerException(cause);
        }

        return t;
    }
}
