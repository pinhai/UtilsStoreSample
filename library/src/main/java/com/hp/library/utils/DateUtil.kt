package com.hp.library.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**

 * Author：admin_h on 2021/4/8 17:43

 日期相关工具类
 */
object DateUtil {

    val DATE_PATTERN_1 = "yyyy-MM-dd"
    val DATE_PATTERN_2 = "yyyy-MM-dd HH:mm:ss"

    fun getYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    fun getDayOfMonth(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    fun getDayOfWeek(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    fun getHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    fun getMinute(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    /**
     * 当天日期 转 字符串，比如 yyyy-MM-dd
     */
    fun getCurrentDateString(pattern: String = DATE_PATTERN_1): String {
        return getDateStringFromMillis(System.currentTimeMillis(), pattern)
    }

    /**
     * 时间戳 转 字符串，比如 yyyy-MM-dd
     */
    fun getDateStringFromMillis(millis: Long, pattern: String = DATE_PATTERN_1): String{
        val formatter = SimpleDateFormat(pattern)
        val curDate = Date(millis)

        return formatter.format(curDate)
    }

    /**
     * 字符串（比如yyyy-MM-dd） 转 时间戳
     */
    fun getMillisFromStr(dataStr: String, pattern: String = DATE_PATTERN_1): Long {
        val format = SimpleDateFormat(pattern)
        var date: Date? = null
        try {
            date = format.parse(dataStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date?.time ?: 0
    }

    /**
     * 指定日期 转 字符串，比如 yyyy-MM-dd
     */
    fun getDateStrFromInt(year: Int, monthOfYear: Int, dayOfMonth: Int): String? {
        return year.toString() + "-" + (if (monthOfYear > 9) monthOfYear else "0$monthOfYear") +
                "-" + if (dayOfMonth > 9) dayOfMonth else "0$dayOfMonth"
    }

    /**
     * 年月 转 Date
     */
    fun getDateFromString(year: Int, month: Int): Date? {
        val dateString = year.toString() + "-" + (if (month > 9) month else "0$month") + "-01"
        var date: Date? = null
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            date = sdf.parse(dateString)
        } catch (e: ParseException) {
            println(e.message)
        }
        return date
    }

    /**
     * 根据年数月数算出日数
     */
    fun getMonthDays(year: Int, month: Int): Int {
        var year = year
        var month = month
        if (month > 12) {
            month = 1
            year += 1
        } else if (month < 1) {
            month = 12
            year -= 1
        }
        val arr = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var days = 0
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            arr[1] = 29 // 闰年2月29天
        }
        try {
            days = arr[month - 1]
        } catch (e: Exception) {
            e.stackTrace
        }
        return days
    }

}