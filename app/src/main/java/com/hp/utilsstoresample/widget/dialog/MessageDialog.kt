package com.hp.utilsstoresample.widget.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.hp.utils_store.widget.dialog.DialogCreator
import com.hp.utilsstoresample.R

/**

 * Author：admin_h on 2021/4/12 21:53

 */
class MessageDialog {

    fun getMessageDialog(
        context: Context?, title: String? = null, msg: String, left: String, right: String,
        cancelOnTouchOutside: Boolean = true, cancelable: Boolean = true,
        gravity: Int = Gravity.CENTER,
        listener: OnMessageDialogClickListener
    ): Dialog? {
        if(context == null) return null

        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_message, null)
        val dialog: Dialog?
        dialog = if (gravity == Gravity.CENTER) {
            DialogCreator.getCenterDialog(context, view)
        } else {
            DialogCreator.getBottomDialog(context, view)
        }
        dialog!!.setCanceledOnTouchOutside(cancelOnTouchOutside)
        dialog!!.setCancelable(cancelable)
        dialog!!.show()
        val tv_title = view.findViewById<TextView>(R.id.tv_title)
        val tv_msg = view.findViewById<TextView>(R.id.tv_message)
        if (TextUtils.isEmpty(title)) {
            tv_title.visibility = View.GONE
        } else {
            tv_title.text = title
        }
        tv_msg.text = msg
        val tv_left = view.findViewById<TextView>(R.id.tv_left)
        if (!TextUtils.isEmpty(left)) {
            tv_left.text = left
        }
        tv_left.setOnClickListener { listener.onLeft(dialog) }
        val tv_right = view.findViewById<TextView>(R.id.tv_right)
        if (!TextUtils.isEmpty(right)) {
            tv_right.text = right
        }
        tv_right.setOnClickListener { listener.onRight(dialog) }
        return dialog
    }

    interface OnMessageDialogClickListener {
        fun onLeft(dialog: Dialog?)
        fun onRight(dialog: Dialog?)
    }

}