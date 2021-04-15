package com.hp.utilsstoresample

import android.app.Application

/**

 * Author：admin_h on 2021/4/12 17:22

 */
class BaseApp : Application() {

    companion object{
        val context
            get() = _context
        private lateinit var _context: Application
    }

    override fun onCreate() {
        super.onCreate()
        _context = this
    }

}