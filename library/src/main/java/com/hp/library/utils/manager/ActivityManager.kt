package com.hp.library.utils.manager

import android.app.Activity
import java.util.*

object ActivityManager {
    private val activityList: MutableList<Activity> = ArrayList()

    fun add(activity: Activity) {
        activityList.add(activity)
    }

    fun removeWithout(clazz: Class<out Activity>) {
        var i = 0
        while (i < activityList.size) {
            val ac = activityList[i]
            if (ac.javaClass != clazz) {
                activityList.remove(ac)
                ac.finish()
            }else{
                i++
            }
        }
    }

    fun removeAll() {
        var i = 0
        while (i < activityList.size) {
            val ac = activityList[i]
            activityList.remove(ac)
            ac.finish()
        }
    }
}