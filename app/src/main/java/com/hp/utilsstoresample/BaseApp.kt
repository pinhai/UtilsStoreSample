package com.hp.utilsstoresample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.hp.utils_store.utils.ScreenUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**

 * Author：admin_h on 2021/4/12 17:22

 */
class BaseApp : Application() {

    companion object{
        val context
            get() = _context
        private lateinit var _context: Application
    }

    override fun onCreate() {
        super.onCreate()
        _context = this
        init()
    }

    @SuppressLint("ResourceType")
    private fun init() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context, refreshLayout: RefreshLayout ->
            refreshLayout.setPrimaryColorsId(android.R.color.darker_gray, R.color.colorPrimary);//全局设置主题颜色
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator(){ context: Context, refreshLayout: RefreshLayout ->
            ClassicsFooter(context).setDrawableSize(16f)
        }

        ScreenUtil.init(context)
    }

}