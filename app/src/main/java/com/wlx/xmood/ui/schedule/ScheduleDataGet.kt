package com.wlx.xmood.ui.schedule

import androidx.lifecycle.liveData
import com.wlx.xmood.utils.TimeUtil.Str2Long
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object ScheduleDataGet {
    val startDate = "2021-03-01"
    val scheduleList = arrayListOf<LessonItem>(
        LessonItem(
            0, "计算理论基础1", "田家炳教育书院236", 1,
            Str2Long("08:00", "HH:mm"),
            Str2Long("09:40", "HH:mm"), 0
        ),
        LessonItem(
            1, "计算理论基础2", "田家炳教育书院236", 2,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm"), 1
        ),
        LessonItem(
            2, "计算理论基础5", "田家炳教育书院236", 5,
            Str2Long("08:00", "HH:mm"),
            Str2Long("10:45", "HH:mm"), 2
        ),
        LessonItem(
            3, "计算理论基础3上午", "田家炳教育书院236", 3,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm"), 0
        ),
        LessonItem(
            4, "计算理论基础1下午", "田家炳教育书院236", 1,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm"), 1
        ),
        LessonItem(
            5, "计算理论基础3下午", "田家炳教育书院236", 3,
            Str2Long("13:00", "HH:mm"),
            Str2Long("15:45", "HH:mm"), 2
        ),
        LessonItem(
            6, "计算理论基础5晚", "田家炳教育书院236", 5,
            Str2Long("18:00", "HH:mm"),
            Str2Long("19:40", "HH:mm"), 0
        )
    )

    fun getSchedule(query: Int) = fire(Dispatchers.IO) {
        if (query == -1) {
            Result.success(scheduleList)
        } else {
            val result = getWeekDaySchedule(query)
            if (result.size == 0) {
                Result.success(null)
            } else {
                Result.success(result)
            }
        }
    }

    private fun getWeekDaySchedule(query: Int): ArrayList<LessonItem> {
        val result = ArrayList<LessonItem>()
        for (schedule in scheduleList) {
            if (query == schedule.weekDay) {
                result.add(schedule)
            }
        }
        return result
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}