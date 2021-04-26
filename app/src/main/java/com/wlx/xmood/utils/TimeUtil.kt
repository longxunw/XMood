package com.wlx.xmood.utils

import java.text.SimpleDateFormat
import java.util.*


object TimeUtil {
    fun Date2Str(date: Date, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.CHINESE)
        return dateFormat.format(date)
    }

    fun Str2Date(string: String, pattern: String): Date {
        val dateFormat = SimpleDateFormat(pattern, Locale.CHINESE)
        return dateFormat.parse(string) as Date
    }

    fun Str2Long(string: String, pattern: String): Long {
        val dateFormat = SimpleDateFormat(pattern, Locale.CHINESE)
        return dateFormat.parse(string)?.time as Long
    }
}