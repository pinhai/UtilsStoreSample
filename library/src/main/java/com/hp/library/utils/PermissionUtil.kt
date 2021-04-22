package com.hp.library.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * 权限工具类
 */
object PermissionUtil {
    private const val TAG = "PermissionUtil"

    const val REQUEST_CODE_PERMISSION_SETTING = 1022

    const val REQUEST_CODE_COARSE_LOCATION = 1001
    const val REQUEST_CODE_RECORD_AUDIO = 1002
    const val REQUEST_CODE_CAMERA = 1003

    /**
     * 启动应用的设置
     */
    fun startAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        (context as Activity).startActivityForResult(intent, REQUEST_CODE_PERMISSION_SETTING)
    }

    /**
     * 申请权限 ,覆盖activity的onRequestPermissionsResult()方法获取结果
     */
    fun requestPermission(context: Context?, perm: Array<String?>, requestCode: Int) {
        if (!checkPermissions(context, perm)) {
            LogUtil.v(TAG, "请求权限:${perm[0]}")
            ActivityCompat.requestPermissions((context as Activity?)!!, perm, requestCode)
        }
    }

    /**
     * fragment中申请权限
     */
    fun requestPermissionFragment(fragment: Fragment, perm: Array<String?>, requestCode: Int) {
        if (!checkPermissions(fragment.activity, perm)) {
            LogUtil.v(TAG, "请求权限:${perm[0]}")
            fragment.requestPermissions(perm, requestCode)
        }
    }

    /**
     * 是否获取到了权限
     */
    fun checkPermissions(context: Context?, perm: Array<String?>): Boolean {
        var checkPermission = 0
        for (p in perm) {
            checkPermission = ContextCompat.checkSelfPermission(context!!, p!!)
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 是否获取到了权限
     */
    fun checkPermission(context: Context?, perm: String?): Boolean {
        return checkPermissions(context, arrayOf(perm))
    }

    /** 23以上版本蓝牙扫描需要定位权限  */
    fun requestCoarseLocationPerm(context: Context?) {
        requestPermission(context, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_COARSE_LOCATION)
    }

    /**
     * 是否获取到了定位权限
     */
    fun checkCoarseLocationPermission(context: Context?): Boolean {
        return checkPermissions(context, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    /**
     * 申请相机拍照权限
     * @param context
     */
    fun requestCameraPerm(context: Context?) {
        requestPermission(context, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
    }

    fun checkCameraPerm(context: Context?): Boolean {
        return checkPermission(context, Manifest.permission.CAMERA)
    }

    /** 申请录音权限  */
    fun requestAudioPerm(context: Context?) {
        requestPermission(context, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE_RECORD_AUDIO)
    }

    /**
     * 是否获取到了录音权限
     */
    fun checkAudioPermission(context: Context?): Boolean {
        return checkPermission(context, Manifest.permission.RECORD_AUDIO)
    }
}