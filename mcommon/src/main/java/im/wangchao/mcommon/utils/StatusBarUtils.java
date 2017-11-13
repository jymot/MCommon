package im.wangchao.mcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import im.wangchao.mcommon.R;

/**
 * <p>Description  : StatusBarUtils.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 2017/8/22.</p>
 * <p>Time         : 下午5:30.</p>
 */
public class StatusBarUtils {
    private static final int KITKAT_STATUS_BAR_COMPAT_VIEW = R.id.kitkat_status_bar_compat_view;

    private StatusBarUtils(){
        throw new AssertionError();
    }

    /**
     * 获取 StatusBar 高度
     */
    public static int getStatusBarHeight(Context context){
        if (!isEnable()){
            return 0;
        }

        final int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取 NavigationBar 高度
     */
    public static int getNavigationBarHeight(Context context){
        if (!isEnable()){
            return 0;
        }
        final int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 设置 NavigationBar 颜色
     */
    public static void setNavigationBarColor(Activity activity, @ColorInt int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            activity.getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * 设置 StatusBar 颜色
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int color){
        if (!isEnable()){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            final View kitkatStatusBar = getKitKatStatusBar(activity);
            kitkatStatusBar.setBackgroundColor(color);
            setRootViewChildFitsSystemWindows(activity);
        }
    }

    /**
     * 隐藏 StatusBar
     */
    public static void hideStatusBar(Activity activity){
        if (!isEnable()){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            final View kitkatStatusBar = getKitKatStatusBar(activity);
            if (kitkatStatusBar != null){
                kitkatStatusBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置透明
     */
    public static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private static View getKitKatStatusBar(Activity activity){
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View kitkatStatusBar = decorView.findViewById(KITKAT_STATUS_BAR_COMPAT_VIEW);
        if (kitkatStatusBar == null){
            kitkatStatusBar = new View(activity);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            kitkatStatusBar.setLayoutParams(params);
            kitkatStatusBar.setId(KITKAT_STATUS_BAR_COMPAT_VIEW);

            decorView.addView(kitkatStatusBar);
        } else {
            if (decorView.getVisibility() == View.GONE){
                decorView.setVisibility(View.VISIBLE);
            }
        }

        return kitkatStatusBar;
    }

    private static void setRootViewChildFitsSystemWindows(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        if (parent == null){
            return;
        }
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    private static boolean isEnable(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
