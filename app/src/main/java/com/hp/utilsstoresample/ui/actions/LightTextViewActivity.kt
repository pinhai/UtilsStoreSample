package com.hp.utilsstoresample.ui.actions

import android.R.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.ui.base.BaseActivity
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
        }
    }

}