package im.wangchao.mcommon.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

/**
 * <p>Description  : AppUtils.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/8/23.</p>
 * <p>Time         : 下午2:23.</p>
 */
public class AppUtils {
    private AppUtils(){
        throw new AssertionError();
    }

    /**
     * Error app version number.
     */
    public static final int APP_ERROR_VERSION = -1;

    /**
     * @return The app version code;
     */
    public static int getAppVersionCode(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    return pi.versionCode;
                }
            } catch (PackageManager.NameNotFoundException e) {
                return APP_ERROR_VERSION;
            }
        }
        return APP_ERROR_VERSION;
    }

    /**
     * @return whether the packageName is on the top of the stack.
     * Notes: You should add android.permission.GET_TASKS in manifest.
     */
    public static boolean isTopActivity(@NonNull Context context, @NonNull String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo == null || tasksInfo.size() == 0) {
            return false;
        }
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return The app is in background.
     */
    public static boolean isAppInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return true;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }

}
