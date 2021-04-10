package com.hp.utils_store.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat

/**

 * Author：admin_h on 2021/4/8 15:39

 应用相关工具类
 */
object AppUtils {

    /**
     * 获取APP版本
     */
    fun getAppVersion(context: Context?): String? {
        if (context == null) return null

        val packageManager = context.packageManager
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packInfo?.versionName
    }

    /**
     * 获取手机IMEI码，需要获取运行时权限
     */
    @SuppressLint("MissingPermission")
    fun getIMEI(context: Context?): String? {
        if (context == null) return null
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !== PackageManager.PERMISSION_GRANTED) {
            null
        } else telephonyManager.deviceId
    }
}