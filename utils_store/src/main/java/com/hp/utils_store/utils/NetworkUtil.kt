package com.hp.utils_store.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException


/**
 * 网络工具类
 */
object NetworkUtil {
    /**
     * 是否存在网络连接，模拟器上可能失效
     */
    fun isNetworkConnected(context: Context?): Boolean {
        if (null == context) {
            return false
        }
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var networks = arrayOfNulls<Network>(0)
            if (connectivity != null) {
                networks = connectivity.allNetworks
            }
            for (network in networks) {
                val capabilities = connectivity!!.getNetworkCapabilities(network)
                if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                    return true
                }
            }
        } else {
            var info: NetworkInfo? = null
            if (connectivity != null) {
                info = connectivity.activeNetworkInfo
            }
            if (info != null && info.isAvailable) {
                return true
            }
        }
        return false
    }

    /**
     * WIFI是否连接
     */
    fun isWifiConnected(context: Context?): Boolean {
        if (null == context) {
            return false
        }
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivity != null) {
                val networks = connectivity.activeNetwork
                val networkCapabilities = connectivity.getNetworkCapabilities(networks)
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    }
                }
            }
        } else {
            val networkInfo = connectivity!!.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * 手机网络是否连接
     */
    fun isMobileConnected(context: Context?): Boolean {
        if (null == context) {
            return false
        }
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivity != null) {
                val networks = connectivity.activeNetwork
                val networkCapabilities = connectivity.getNetworkCapabilities(networks)
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    }
                }
            }
        } else {
            val networkInfo = connectivity!!.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    return true
                }
            }
        }

        return false
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