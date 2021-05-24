package com.wlx.xmood.utils

import android.util.Log
import com.wlx.xmood.ui.schedule.LessonItem
import java.text.ParseException
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

    fun getCurrentWeekDate(): List<String> {
        val result: MutableList<String> = ArrayList()
        val date = Date()
        val sdf = SimpleDateFormat("MM/dd")
        val cal = Calendar.getInstance()
        cal.time = date
        val dayWeek = cal[Calendar.DAY_OF_WEEK]
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1)
        }
        cal.firstDayOfWeek = Calendar.MONDAY
        val day = cal[Calendar.DAY_OF_WEEK]
        cal.add(Calendar.DATE, cal.firstDayOfWeek - day)
        val monday = sdf.format(cal.time)
        result.add(monday)
        for (i in 1..6) {
            cal.add(Calendar.DATE, 1)
            result.add(sdf.format(cal.time))
        }
        return result
    }

    @Throws(ParseException::class)
    fun getEndPeriod(lessonItem: LessonItem): Int {
        val endTime = lessonItem.endTime
        val period1 = Str2Long("08:45", "HH:mm")
        val period2 = Str2Long("09:40", "HH:mm")
        val period3 = Str2Long("10:45", "HH:mm")
        val period4 = Str2Long("11:40", "HH:mm")
        val period5 = Str2Long("13:45", "HH:mm")
        val period6 = Str2Long("14:40", "HH:mm")
        val period7 = Str2Long("15:45", "HH:mm")
        val period8 = Str2Long("16:40", "HH:mm")
        val period9 = Str2Long("18:45", "HH:mm")
        val period10 = Str2Long("19:40", "HH:mm")
        if (endTime == period1) {
            return 1
        } else if (endTime == period2) {
            return 2
        } else if (endTime == period3) {
            return 3
        } else if (endTime == period4) {
            return 4
        } else if (endTime == period5) {
            return 5
        } else if (endTime == period6) {
            return 6
        } else if (endTime == period7) {
            return 7
        } else if (endTime == period8) {
            return 8
        } else if (endTime == period9) {
            return 9
        } else if (endTime == period10) {
            return 10
        }
        return 0
    }

    @Throws(ParseException::class)
    fun getStartPeriod(lessonItem: LessonItem): Int {
        val startTime = lessonItem.startTime
        val period1 = Str2Long("08:00", "HH:mm")
        val period2 = Str2Long("08:55", "HH:mm")
        val period3 = Str2Long("10:00", "HH:mm")
        val period4 = Str2Long("10:55", "HH:mm")
        val period5 = Str2Long("13:00", "HH:mm")
        val period6 = Str2Long("13:55", "HH:mm")
        val period7 = Str2Long("15:00", "HH:mm")
        val period8 = Str2Long("15:55", "HH:mm")
        val period9 = Str2Long("18:00", "HH:mm")
        val period10 = Str2Long("18:55", "HH:mm")
        if (startTime == period1) {
            return 1
        } else if (startTime == period2) {
            return 2
        } else if (startTime == period3) {
            return 3
        } else if (startTime == period4) {
            return 4
        } else if (startTime == period5) {
            return 5
        } else if (startTime == period6) {
            return 6
        } else if (startTime == period7) {
            return 7
        } else if (startTime == period8) {
            return 8
        } else if (startTime == period9) {
            return 9
        } else if (startTime == period10) {
            return 10
        }
        return 0
    }
}