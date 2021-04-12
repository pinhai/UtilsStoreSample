package com.hp.utils_store.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat

/**

 * Author：admin_h on 2021/4/8 15:39

 应用相关工具类
 */
object AppUtil {

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

    /**
     * 判断用户是否开启了模拟位置功能
     */
    fun checkMockLocation(context: Context): Boolean {
        var isOpen = Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.ALLOW_MOCK_LOCATION,
            0
        ) != 0
        /**
         * 该判断API是androidM以下的API,由于Android M中已经没有了关闭允许模拟位置的入口,所以这里一旦检测到开启了模拟位置,并且是android M以上,则
         * 默认设置为未有开启模拟位置
         */
        if (isOpen && Build.VERSION.SDK_INT > 22) {
            isOpen = false
        }
        return isOpen
    }
}