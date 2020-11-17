package com.uweic.lib_common.utils;
import android.content.Context;

/**
 * Created by haoxuhong on 2019/10/14.
 *
 * @description: 常用单位转换的辅助类
 */



public class MyDp2Px {
    private MyDp2Px() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }



    /**
     * 单位转换: dp -> px
     *
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) (getDensity(context) * dp + 0.5);
    }

    /**
     * 单位转换: sp -> px
     *
     * @param sp
     * @return
     */
    public static int sp2px(Context context, float sp) {
        return (int) (getFontDensity(context) * sp + 0.5);
    }

    /**
     * 单位转换:px -> dp
     *
     * @param px
     * @return
     */
    public static int px2dp(Context context, float px) {
        return (int) (px / getDensity(context) + 0.5);
    }

    /**
     * 单位转换:px -> sp
     *
     * @param px
     * @return
     */
    public static int px2sp(Context context, float px) {
        return (int) (px / getFontDensity(context) + 0.5);

    }
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
    public static float getFontDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }
}
