package com.hp.utilsstoresample.ui.actions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.ui.base.BaseActivity

/**
 *
 * Author：admin_h on 2021/4/20 21:11
 */
class LightTextViewActivity: BaseActivity() {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, LightTextViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_textview)
    }

}