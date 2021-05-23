package com.wlx.xmood.ui.schedule

import androidx.lifecycle.liveData
import com.wlx.xmood.utils.TimeUtil.Str2Long
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object ScheduleDataGet {
    val scheduleList = arrayListOf<LessonItem>(
        LessonItem(0, "计算理论基础1", "田家炳教育书院236", 1, 1,
            Str2Long("08:00", "HH:mm"),
            Str2Long("09:40", "HH:mm")
        ),
        LessonItem(1, "计算理论基础2", "田家炳教育书院236", 2, 2,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm")
        ),
        LessonItem(2, "计算理论基础5", "田家炳教育书院236", 5, 3,
            Str2Long("08:00", "HH:mm"),
            Str2Long("10:45", "HH:mm")
        ),
        LessonItem(3, "计算理论基础3上午", "田家炳教育书院236", 3, 4,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm")
        ),
        LessonItem(4, "计算理论基础1下午", "田家炳教育书院236", 1, 4,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm")
        ),
        LessonItem(5, "计算理论基础3下午", "田家炳教育书院236", 3, 4,
            Str2Long("13:00", "HH:mm"),
            Str2Long("15:45", "HH:mm")
        ),
        LessonItem(6, "计算理论基础5晚", "田家炳教育书院236", 5, 4,
            Str2Long("18:00", "HH:mm"),
            Str2Long("19:40", "HH:mm")
        ),
        LessonItem(7, "计算理论基础5晚", "田家炳教育书院236", 1, 4,
            Str2Long("18:00", "HH:mm"),
            Str2Long("19:40", "HH:mm")
        )
    )

    fun getSchedule() = fire(Dispatchers.IO) {
        val result = scheduleList
        if (result == null) {
            Result.success(null)
        } else {
            Result.success(result)
        }
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