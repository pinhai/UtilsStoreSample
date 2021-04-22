package com.hp.library.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.hp.library.R
import com.hp.library.utils.ScreenUtil

/**

 * Author：admin_h on 2021/4/11 23:52
    自定义视图对话框创建器
 */
object DialogCreator {

    /**
     * 中间对话框
     */
    fun getCenterDialog(context: Context?, view: View?): Dialog? {
        if(context == null || view == null) return null
        val dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(view!!)
        dialog.setCanceledOnTouchOutside(false)
        val window = dialog.window
        if (dialog != null && window != null) {
            window.decorView.setPadding(0, 0, 0, 0)
            val attr = window.attributes
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT
                attr.width = ScreenUtil.getScreenWidth() * 3 / 4
                attr.gravity = Gravity.CENTER //设置dialog 在布局中的位置
                window.attributes = attr
            }
        }
        return dialog
    }

    /**
     * 底部对话框
     */
    fun getBottomDialog(
        context: Context, view: View,
        height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    ): Dialog {
        val dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window
        if (dialog != null && window != null) {
            window.decorView.setPadding(0, 0, 0, 0)
            val attr = window.attributes
            if (attr != null) {
                attr.height = height
                attr.width = ViewGroup.LayoutParams.MATCH_PARENT
                attr.gravity = Gravity.BOTTOM //设置dialog 在布局中的位置
                window.attributes = attr
            }
        }
        return dialog
    }

}