package com.hp.utils_store.utils

/**

 * Author：admin_h on 2021/4/12 19:00
    扩展函数工具类
 */

inline fun <reified T> T.getClassName(): String = T::class.java.name