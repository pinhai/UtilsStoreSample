package com.hp.utils_store.utils

import android.text.TextUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * MD5加密
 */
object MD5Utils {
    //十六进制
    private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * MD5编码，返回十六进制字符串
     */
    fun encode(text: String): String? {
        if (TextUtils.isEmpty(text)) return null
        val btInput = text.toByteArray()
        // 获得MD5摘要算法的 MessageDigest 对象
        var mdInst: MessageDigest? = null
        try {
            mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            val md = mdInst.digest()
            // 把密文转换成十六进制的字符串形式
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigits[(byte0.toInt() ushr 4 and 0xf)]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            return String(str)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

}