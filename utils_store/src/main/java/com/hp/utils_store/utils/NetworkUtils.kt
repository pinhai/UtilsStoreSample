package com.hp.utils_store.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import java.io.UnsupportedEncodingException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URLEncoder

/**
 * 网络工具类
 */
object NetworkUtils {
    /**
     * 是否存在网络连接
     */
    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**
     * WIFI是否连接
     */
    fun isWifiConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**
     * 手机网络是否连接
     */
    fun isMobileConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**
     * 当前网络类型
     */
    fun getConnectedType(context: Context?): Int {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null && mNetworkInfo.isAvailable) {
                return mNetworkInfo.type
            }
        }
        return -1
    }

    /**
     * 检测网络，没有网络弹出对话框提醒
     */
    fun checkNetworkShowDialog(activity: Activity): Boolean {
        if (!isNetworkConnected(activity)) {
            dialog(activity)
            return false
        }
        return true
    }

    /**
     * 弹出dialog
     */
    private fun dialog(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("网络连接没有打开！")
        builder.setTitle("提示")
        builder.setPositiveButton("确认") { dialog, which -> dialog.dismiss() }
        val dialog: Dialog = builder.create()
        dialog.setOnDismissListener { activity.finish() }
        dialog.show()
    }

    /**
     * 在URL后连接参数，主要是GET方式时使用
     */
    fun connectParams(baseUrl: String, map: Map<String?, String?>?): String {
        var baseUrl = baseUrl
        if (map == null) return baseUrl
        val param = StringBuffer()
        var i = 0
        var para: String?
        for (key in map.keys) {
            if (map[key] == null) continue
            if (i == 0 && baseUrl.indexOf('?') == -1) param.append("?") else param.append("&")

            // param.append(key).append("=").append(map.get(key));
            try {
                para = URLEncoder.encode(map[key], "utf-8")
                param.append(key).append("=").append(para)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            i++
        }
        baseUrl += param
        Log.i("NetworkUtils", "路径: $baseUrl")
        return baseUrl
    }

    fun getIPAddress(context: Context): String? {
        val info = (context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info != null && info.isConnected) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) { //当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                return inetAddress.getHostAddress()
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }
            } else if (info.type == ConnectivityManager.TYPE_WIFI) { //当前使用无线网络
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                return intIP2StringIP(wifiInfo.ipAddress)
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }
}