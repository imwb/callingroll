package com.example.wb.calling.utils;

import android.content.Context;

/**
 * Created by wb on 16/2/4.
 */
public class DisplayUtil {

    public static int dp2px(Context context,float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
