package im.wangchao.mcommon.utils;

import android.content.Context;
import android.util.TypedValue;

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
