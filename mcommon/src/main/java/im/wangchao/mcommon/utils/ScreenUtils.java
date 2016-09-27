package im.wangchao.mcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Description  : ScreenUtils.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/13.</p>
 * <p>Time         : 上午9:50.</p>
 */
public class ScreenUtils {
    private ScreenUtils(){
        throw new AssertionError();
    }

    public static final int XXHDPI = 1;//超高分辨率     1080×1280
    public static final int XHDPI = 2;//超高分辨率      720×1280
    public static final int HDPI = 3;//高分辨率         480×800
    public static final int MDPI = 4;//中分辨率         320×480
    public static final int LDPI = 5;//低分辨率

    @IntDef({
            XXHDPI, XHDPI, HDPI, MDPI, LDPI
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenDensity{}

    @ScreenDensity public static int initDisplay(@NonNull Context context){
        int eScreenDensity;
        //初始化屏幕密度
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        if (densityDpi <= 160){
            eScreenDensity = MDPI;
        }else if (densityDpi <= 240){
            eScreenDensity = HDPI;
        }else if (densityDpi < 400){
            eScreenDensity = XHDPI;
        }else {
            eScreenDensity = XXHDPI;
        }

        return  eScreenDensity;
    }

    /**
     * @return Whether to pad.
     */
    public static boolean isPad(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        //屏幕尺寸(英寸)
        double screenInches = Math.sqrt(x + y);
        //大于6英寸则为Pad
        return screenInches >= 6.0;
    }

    /**
     * Set the {@code activity} disable screenshot.
     */
    public static void disabledScreenshot(@NonNull Activity activity){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    /**
     * Cancel disable screenshot.
     */
    public static void cancelDisabledScreenshot(@NonNull Activity activity){
        if (isDisabledScreenshot(activity)){
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    /**
     * @return Whether to disable screenshot.
     */
    public static boolean isDisabledScreenshot(@NonNull Activity activity){
        int flag = activity.getWindow().getAttributes().flags;
        return (flag & WindowManager.LayoutParams.FLAG_SECURE)
                == WindowManager.LayoutParams.FLAG_SECURE;
    }

    /**
     * Set the {@code activity} full screen.
     */
    public static void setFullScreen(@NonNull Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * If the current {@code activity} for full screen, then cancel.
     */
    public static void cancelFullScreen(@NonNull Activity activity) {
        if (isFullScreen(activity)){
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * @return Whether the current {@code activity} full screen.
     */
    public static boolean isFullScreen(@NonNull Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        return (flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * @return Whether this current {@code activity} vertical screen.
     */
    public static boolean isVerticalScreen(@NonNull Activity activity) {
        int flag = activity.getResources().getConfiguration().orientation;
        return flag != 0;
    }

    /**
     * @return Status bar height.
     */
    public static int getStatusBarHeight(@NonNull Context context) {
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
     * @return Screen size, int[0] = width, int[1] = height.
     */
    public static int[] getScreenCompat(@NonNull Activity activity) {
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
     * @return Screen width.
     */
    public static int getScreenWidth(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * @return Screen height.
     */
    public static int getScreenHeight(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
