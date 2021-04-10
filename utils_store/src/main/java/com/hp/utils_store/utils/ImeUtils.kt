package com.hp.utils_store.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 输入法工具类
 */
object ImeUtils {
    /**
     * 开关输入法
     */
    fun toggleInputMethod(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput(view: View?) {
        if (view == null) return
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    fun showSoftInput(context: Context, view: View?) {
        if (view == null) return
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        view.postDelayed(Runnable { imm.showSoftInput(view, InputMethodManager.SHOW_FORCED) }, 500)
    }
}