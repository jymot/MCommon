package im.wangchao.mcommon.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * <p>Description  : DimenUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/9.</p>
 * <p>Time         : 下午9:37.</p>
 */
public class DimenUtils {
    private DimenUtils(){}

    /**
     * check pad
     */
    public static boolean isPad(Context context) {
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
     * dp -> px
     */
    public static int dp2px(Context context, float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    /**
     * px -> dp
     */
    public static int px2dp(Context context, float px){
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    /**
     * sp -> px
     */
    public static int sp2px(Context context, float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics());
    }

    /**
     * px -> sp
     */
    public static int px2sp(Context context, float px){
        return (int) (px / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
