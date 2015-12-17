package im.wangchao.mcommon.utils;

/**
 * <p>Description  : AssertUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/17.</p>
 * <p>Time         : 下午2:10.</p>
 */
public class AssertUtils {
    private AssertUtils(){
        throw new AssertionError();
    }

    /**
     * asserts
     */
    public static void asserts(final boolean expression, final String failedMessage) {
        if (!expression) {
            throw new AssertionError(failedMessage);
        }
    }
}
