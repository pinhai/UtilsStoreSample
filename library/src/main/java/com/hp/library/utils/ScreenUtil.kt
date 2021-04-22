package com.hp.library.utils

import android.content.Context

/**

 * Author：admin_h on 2021/4/8 18:35
    屏幕相关工具类
 */
object ScreenUtil {

    private lateinit var context : Context
    private var screenWidth = 0
    private var screenHeight = 0
    //屏幕密度
    private var densityDpi = 0
    //逻辑密度, 屏幕缩放因子
    private var density = 0f

    /**
     * 放入application
     * @param context
     */
    fun init(context: Context) {
        this.context = context
        val dm = context.resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        densityDpi = dm.densityDpi
        density = dm.density
    }

    fun getScreenHeight(): Int = screenHeight

    fun getScreenWidth(): Int = screenWidth

    fun getStatusBarHeight(): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        LogUtil.v("dbw", "Status height:$height")
        return height
    }

    private fun getNavigationBarHeight(): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        LogUtil.v("dbw", "Navi height:$height")
        return height
    }

    fun px2dp(inParam: Float): Int = (inParam / density + 0.5f).toInt()

    fun dp2px(inParam: Float): Int = (inParam * density + 0.5f).toInt()

    fun px2sp(inParam: Float): Int = (inParam / density + 0.5f).toInt()

    fun sp2px(inParam: Float): Int = (inParam * density + 0.5f).toInt()

}