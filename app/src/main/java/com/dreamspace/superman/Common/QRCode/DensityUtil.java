package com.dreamspace.superman.Common.QRCode;

import android.content.Context;

/**
 * Created by Wells on 2016/2/16.
 */
public class DensityUtil {

    public static int dp2px(Context context, float dpVal) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dpVal + 0.5f);
    }

    public static int px2dp(Context context, float pxVal) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / density + 0.5f);
    }
}
