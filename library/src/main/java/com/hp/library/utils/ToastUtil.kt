package com.hp.library.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.hp.library.R

object ToastUtil {
    fun show(context: Context?, res: Int?) {
        if(context == null || res == null) return
        show(context, context.getString(res))
    }

    fun show(context: Context?, toast: String?) {
        if(context == null || toast == null) return
        val `in` = LayoutInflater.from(context)
        val view = `in`.inflate(R.layout.view_toast, null)
        val tv = view.findViewById<TextView>(R.id.toast)
        tv.text = toast
        val t = Toast(context)
        t.view = view
        t.setGravity(Gravity.CENTER, 0, 0)
        t.show()
    }

}