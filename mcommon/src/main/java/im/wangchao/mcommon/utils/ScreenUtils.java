package im.wangchao.mcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * <p>Description  : ScreenUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/13.</p>
 * <p>Time         : 上午9:50.</p>
 */
public class ScreenUtils {
    private ScreenUtils(){}

    /**
     * 设置当前界面为全屏模式
     */
    public static void setFullScreen(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 如果当前为全屏，那么取消全屏模式，回到正常的模式
     */
    public static void cancelFullScreen(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 判断当前手机是否是全屏
     *
     * @return 如果是true，那么当前就是全屏
     */
    public static boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        return (flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 判断当前屏幕是否是横屏
     *
     * @param activity 当前的activity
     * @return 如果true就是竖屏
     */
    public static boolean isVerticalScreen(Activity activity) {
        int flag = activity.getResources().getConfiguration().orientation;
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取顶部状态栏高度
     *
     * @return 顶部状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取屏幕长宽,
     *
     * @return 装载了屏幕长宽的数组，int[0] = width,int[1] = height
     */
    public static int[] getScreenCompat(Activity activity) {
        //>= API13
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return new int[]{size.x, size.y};
        } else {
            WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            int w = outMetrics.widthPixels;//获得手机屏幕的宽度
            int h = outMetrics.heightPixels;//获得手机屏幕的高度
            return new int[]{w, h};
        }
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
