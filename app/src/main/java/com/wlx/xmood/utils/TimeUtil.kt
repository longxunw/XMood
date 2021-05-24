package com.wlx.xmood.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


object TimeUtil {
    private val TAG = "TimeUtil"
    fun Date2Str(date: Date, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
        val string = dateFormat.format(date)
        Log.d(TAG, "Date2Str: $string")
        return string
    }

    fun Str2Date(string: String, pattern: String): Date {
        val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
        Log.d(TAG, "Str2Date: $string")
        return dateFormat.parse(string) as Date
    }

    fun Str2Long(string: String, pattern: String): Long {
        val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
        return dateFormat.parse(string)?.time as Long
    }

    fun getWeekCount(date: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val start: Long = (dateFormat.parse(date)?.time as Long) / (1000 * 3600 * 24)
        val now: Long = (Date().time) / (1000 * 3600 * 24)
        return (now - start) / 7 + 1
    }

    fun getWeekDayChinese(weekDay: Int): String {
        val weekDayMap = mapOf(0 to "日", 1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六")
        return weekDayMap[weekDay]!!
    }
}