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
}