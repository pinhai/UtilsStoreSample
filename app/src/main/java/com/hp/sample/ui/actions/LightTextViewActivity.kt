package com.hp.sample.ui.actions

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.hp.library.utils.LogUtil
import com.hp.library.utils.getClassName
import com.hp.sample.R
import com.hp.sample.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_light_textview.*

/**
 *
 * Author：admin_h on 2021/4/20 21:11
 */
class LightTextViewActivity: BaseActivity(), View.OnClickListener {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, LightTextViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_textview)
    }

    private var count: Int = 0
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_ok -> {
                ltv.setText(edittext.text.toString())
            }
            R.id.btn_changeColor -> {
                val color = ltv.getTextColor()
                val resColor = resources.getColor(android.R.color.holo_red_light)
                if(color == resColor){
                    ltv.setTextColor(Color.BLACK)
                }else{
                    ltv.setTextColor(resColor)
                }
            }
            R.id.btn_changeDrawable -> {
                LogUtil.d(getClassName(), "count%3:${count%3}")
                when(count % 3) {
                    0 -> {
                        ltv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_avater_flag_2, R.drawable.ic_avater_flag_3)
                    }
                    1 -> {
                        ltv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_avater_flag_3, R.drawable.ic_avater_flag_2)
                    }
                    else -> {
                        ltv.setCompoundDrawablesWithIntrinsicBounds()
                    }
                }
                count++
            }
        }
    }

}