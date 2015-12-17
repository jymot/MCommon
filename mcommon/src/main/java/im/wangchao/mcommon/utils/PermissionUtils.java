package im.wangchao.mcommon.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * <p>Description  : PermissionUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/17.</p>
 * <p>Time         : 下午2:38.</p>
 */
public class PermissionUtils {
    private PermissionUtils() {
        throw new AssertionError();
    }

    /**
     * check permission
     */
    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(permission);
            return PackageManager.PERMISSION_GRANTED == result;
        } else {
            return true;
        }
    }
}
