package com.hp.utils_store.utils

import android.content.Context
import java.io.File

/**
 * Author：admin_h on 2021/4/8 17:21

    目录管理器

    Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
 */
object DirectoryManager {

    /**
     * 返回缓存文件目录
     */
    fun getCacheDir(context : Context) : File? = context.externalCacheDir

    /**
     * 返回长期存在的文件目录
     */
    fun getFileDir(context: Context, type: String? = null) : File? = context.getExternalFilesDir(type)

}