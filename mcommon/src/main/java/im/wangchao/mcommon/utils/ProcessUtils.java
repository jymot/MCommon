package im.wangchao.mcommon.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.FileInputStream;


/**
 * <p>Description  : ProcessorUtils.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/11/9.</p>
 * <p>Time         : 上午10:12.</p>
 */
public class ProcessUtils {
    private static final String TAG = ProcessUtils.class.getSimpleName();
    private ProcessUtils(){
        throw new AssertionError();
    }

    private static String mCacheProcessName = null;

    /**
     * Kill current process.
     */
    public static void killMyProcess(){
        killProcess(Process.myPid());
    }

    /**
     * Kill the process of {@code pid}.
     * @param pid target process id.
     */
    public static void killProcess(int pid){
        Process.killProcess(pid);
    }

    /**
     * @return is in main process.
     */
    public static boolean isInMainProcess(final Context context) {
        String pkgName = context.getPackageName();
        String processName = getMyProcessName(context);
        if (processName == null || processName.length() == 0) {
            processName = "";
        }

        return pkgName.equals(processName);
    }

    /**
     * Kill all other process.
     */
    public static void killAllOtherProcess(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // NOTE: getRunningAppProcess() ONLY GIVE YOU THE PROCESS OF YOUR OWN PACKAGE IN ANDROID M
        // BUT THAT'S ENOUGH HERE
        for (ActivityManager.RunningAppProcessInfo ai : am.getRunningAppProcesses()) {
            // KILL OTHER PROCESS OF MINE
            if (ai.uid == Process.myUid() && ai.pid != Process.myPid()) {
                killProcess(ai.pid);
            }
        }
    }

    /**
     * @return Current process name.
     */
    public static String getMyProcessName(final Context context){
        int myPid = Process.myPid();
        return getProcessName(context, myPid);
    }

    /**
     * @return Target process name with {@code pid}.
     */
    public static String getProcessName(final Context context, int pid) {
        if (mCacheProcessName != null) {
            return mCacheProcessName;
        }
        mCacheProcessName = getProcessNameInternal(context, pid);
        return mCacheProcessName;
    }

    /**
     * The real get process name method.
     */
    private static String getProcessNameInternal(final Context context, int pid) {

        if (context == null || pid <= 0) {
            return "";
        }

        ActivityManager.RunningAppProcessInfo myProcess = null;
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        try {
            for (ActivityManager.RunningAppProcessInfo process : activityManager.getRunningAppProcesses()) {
                if (process.pid == pid) {
                    myProcess = process;
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getProcessName(" + pid + ") exception:" + e.getMessage());
        }

        if (myProcess != null) {
            return myProcess.processName;
        }

        byte[] b = new byte[128];
        FileInputStream in = null;
        try {
            in = new FileInputStream("/proc/" + pid + "/cmdline");
            int len = in.read(b);
            if (len > 0) {
                for (int i = 0; i < len; i++) { // lots of '0' in tail , remove them
                    if (b[i] > 128 || b[i] <= 0) {
                        len = i;
                        break;
                    }
                }
                return new String(b, 0, len);
            }

        } catch (Exception ignore) {
        } finally {
            IOUtils.closeQuietly(in);
        }

        return "";
    }
}
