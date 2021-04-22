package com.hp.sample.widget.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.hp.library.utils.LogUtil
import com.hp.library.utils.ScreenUtil
import com.hp.library.utils.getClassName
import com.squareup.picasso.Picasso
import kotlin.math.abs

/**
 * 通过计算滚动速度来判断暂停加载或者恢复加载图片
 * Author：admin_h on 2021/4/19 23:16
 */
class ImageLoadScrollListener(private val tag: Any) : RecyclerView.OnScrollListener() {

    private var speed = 0 //速度，像素/s
    private var lastMillis = 0L  //上一次的时间戳
    private var isPauseLoadImage = false //是否已暂停加载图片

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        LogUtil.d(getClassName(), "state:$newState")
        when(newState){
            SCROLL_STATE_IDLE -> {
                lastMillis = 0
                if(isPauseLoadImage){
                    resumeLoadImage()
                }
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        LogUtil.d(getClassName(), "onScrolled(),dx:$dx,dy:$dy")
        if(lastMillis == 0L){
            lastMillis = System.currentTimeMillis()
        }else{
            val intervalTime = System.currentTimeMillis() - lastMillis
            speed = (abs(dy).toFloat() / intervalTime * 1000).toInt()
            LogUtil.d(getClassName(), "speed:$speed,阈值pause:$SPEED_THRESHOLD_PAUSE,阈值resume:$SPEED_THRESHOLD_RESUME")
            if(speed > SPEED_THRESHOLD_PAUSE){
                if(!isPauseLoadImage){
                    pauseLoadImage()
                }
            }else if(isPauseLoadImage && speed < SPEED_THRESHOLD_RESUME){
                resumeLoadImage()
            }
            lastMillis += intervalTime
        }
    }

    private fun pauseLoadImage() {
        LogUtil.d(getClassName(), "暂停加载图片")
        Picasso.get().pauseTag(tag)
        isPauseLoadImage = true
    }

    private fun resumeLoadImage() {
        LogUtil.d(getClassName(), "恢复加载图片")
        Picasso.get().resumeTag(tag)
        isPauseLoadImage = false
    }

    companion object {
        //速度阈值，超过则不加载图片
        private val SPEED_THRESHOLD_PAUSE = ScreenUtil.dp2px(800F)
        //速度阈值，如果进入不加载图片状态，则滚动速度需要小于该值才开始恢复加载
        private val SPEED_THRESHOLD_RESUME = ScreenUtil.dp2px(200F)
    }
}