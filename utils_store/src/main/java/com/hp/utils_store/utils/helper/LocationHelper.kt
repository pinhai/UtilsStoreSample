package com.hp.utils_store.utils.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.hp.utils_store.R
import com.hp.utils_store.utils.AppUtil
import com.hp.utils_store.utils.LogUtil
import com.hp.utils_store.utils.PermissionUtil
import com.hp.utils_store.utils.getClassName

/**
 * 获取用户的地理位置
 */
class LocationHelper(context: Context) {
    private var locationManager: LocationManager

    private var mContext: Context = context
    private var mFragment: Fragment? = null
    private var listener: OnLocationResultListener? = null

    private var locationListener : LocationListener? = null
    //更新定位的最小时间，单位毫秒
    private var minTimeMs = 10*60*1000L
    //更新定位的最小距离，单位米
    private var minDistanceM = 200f

    private val permission = arrayOf<String?>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    init {
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    constructor(fragment: Fragment) : this(fragment.context!!){
        mFragment = fragment
    }

    companion object {
        const val REQUEST_CODE_LOCATION_PERM = 101
    }

    /**
     * 将该方法放入Activity或Fragment的onRequestPermissionsResult方法中
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERM -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获取权限成功
                location()
            } else {
                // 获取权限失败
                locationFailed()
            }
        }
    }

    /**
     * 请求位置信息
     */
    fun requestLocation(listener: OnLocationResultListener?) {
        if(mContext == null) return
        this.listener = listener
        if (AppUtil.checkMockLocation(mContext!!)) {
            showMockPromptDialog()
            return
        }

        if (!isLocationServiceEnable() && Build.VERSION.SDK_INT >= 28) {
            showOpenLocationServiceDialog()
            return
        }

        val hasPerm: Boolean = PermissionUtil.checkPermissions(mContext, permission)
        if (!hasPerm && Build.VERSION.SDK_INT >= 23) {
            if(mFragment == null){
                PermissionUtil.requestPermission(mContext, permission, REQUEST_CODE_LOCATION_PERM)
            }else{
                PermissionUtil.requestPermissionFragment(mFragment!!, permission, REQUEST_CODE_LOCATION_PERM)
            }
        } else {
            location()
        }
    }

    private var mockPromptDialog: Dialog? = null
    private fun showMockPromptDialog() {
        mockPromptDialog = AlertDialog.Builder(mContext)
            .setMessage(mContext.getString(R.string.close_mock_location_tip))
            .setPositiveButton(R.string.to_set) { dialogInterface: DialogInterface, i: Int ->
                //打开设置界面
                mContext.startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
            }
            .setCancelable(false)
            .show()
    }

    /**
     * 放到定位activity或者Fragment的onRestart方法中
     */
    fun checkMockLocation() {
        if (mContext != null && !AppUtil.checkMockLocation(mContext) && mockPromptDialog != null && mockPromptDialog!!.isShowing) {
            mockPromptDialog!!.dismiss()
            requestLocation(listener)
        }
    }

    private fun showOpenLocationServiceDialog() {
        AlertDialog.Builder(mContext)
            .setTitle(R.string.tip)
            .setMessage(R.string.not_open_location_service_tip)
            .setNegativeButton(R.string.cancel){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton(R.string.to_set){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                mContext!!.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    //定位
    @SuppressLint("MissingPermission")
    private fun location() {
        //前面已经判断这两种方式至少有一种可用
        val provider = if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) LocationManager.GPS_PROVIDER
                        else LocationManager.NETWORK_PROVIDER
        if(locationListener == null){
            locationListener = LocationListener{
                locationSuccessful(it)
            }
        }
        locationManager.requestLocationUpdates(provider, minTimeMs, minDistanceM, locationListener!!)

    }

    private fun locationSuccessful(it: Location) {
        LogUtil.v(getClassName(), "${it.longitude},${it.latitude}")
        listener?.onLocationResult(it)
    }

    private fun locationFailed() {
        LogUtil.v(getClassName(), "定位失败")
        listener?.onLocationFailed()
    }

    fun stopLocation() {
        if(locationListener != null){
            locationManager.removeUpdates(locationListener!!)
        }
    }

    interface OnLocationResultListener {
        fun onLocationResult(location: Location?)
        fun onLocationFailed()
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    private fun isLocationServiceEnable(): Boolean {
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

}