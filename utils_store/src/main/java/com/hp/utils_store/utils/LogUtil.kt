package com.hp.utils_store.utils

import android.util.Log

/**

 * Author：admin_h on 2021/4/12 15:56
    日志工具类
 */
object LogUtil {

    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARNING = 4
    private const val ERROR = 5
    private const val level = VERBOSE  //发布时可改成ERROR，这样只会打印错误信息

    fun v(tag: String, msg: String){
        if(level <= VERBOSE) Log.v(tag, msg)
    }

    fun d(tag: String, msg: String){
        if(level <= DEBUG) Log.d(tag, msg)
    }

    fun i(tag: String, msg: String){
        if(level <= INFO) Log.i(tag, msg)
    }

    fun w(tag: String, msg: String){
        if(level <= WARNING) Log.w(tag, msg)
    }

    fun e(tag: String, msg: String){
        if(level <= ERROR) Log.e(tag, msg)
    }

}