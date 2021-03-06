package com.hp.library.utils

import android.content.Context
import android.graphics.Typeface

/**
 * 字体工具类
 */
object TypefaceUtil {
    /**
     * 通过{TextView.setTypeface()}设置
     */
    var condensed: Typeface? = null
        private set

    /**
     * 在Application初始化
     */
    fun init(context: Context) {
        condensed = Typeface.createFromAsset(context.assets, "fonts/DIN Condensed Bold.ttf")
    }
}