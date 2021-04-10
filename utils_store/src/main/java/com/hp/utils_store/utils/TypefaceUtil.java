package com.hp.utils_store.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * 字体工具类
 */
public class TypefaceUtil {

    private static Typeface condensed;

    /**
     * 在Application初始化
     */
    public static void init(Context context){
        condensed = Typeface.createFromAsset(context.getAssets(), "fonts/DIN Condensed Bold.ttf");
    }

    /**
     * 通过{TextView.setTypeface()}设置
     */
    public static Typeface getCondensed(){
        return condensed;
    }

}
