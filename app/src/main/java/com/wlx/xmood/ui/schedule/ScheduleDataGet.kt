package com.wlx.xmood.ui.schedule

import androidx.lifecycle.liveData
import com.wlx.xmood.utils.TimeUtil.Str2Long
import com.wlx.xmood.utils.TimeUtil.getWeekCount
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object ScheduleDataGet {
    private var id = 10
    var startDate = ""
    val scheduleList = arrayListOf<LessonItem>(
        LessonItem(
            0, "计算理论基础1", "田家炳教育书院236", 1,
            Str2Long("08:00", "HH:mm"),
            Str2Long("09:30", "HH:mm"), 0, 1, 17
        ),
        LessonItem(
            1, "计算理论基础2双周", "田家炳教育书院236", 2,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:30", "HH:mm"), 2, 1, 17
        ),
        LessonItem(
            8, "计算理论基础2单周", "田家炳教育书院236", 2,
            Str2Long("13:00", "HH:mm"),
            Str2Long("14:40", "HH:mm"), 1, 1, 17
        ),
        LessonItem(
            2, "计算理论基础5", "田家炳教育书院236", 5,
            Str2Long("08:00", "HH:mm"),
            Str2Long("10:45", "HH:mm"), 2, 11, 17
        ),
        LessonItem(
            3, "计算理论基础3上午", "田家炳教育书院236", 3,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm"), 0, 13, 17
        ),
        LessonItem(
            4, "计算理论基础1上午", "田家炳教育书院236", 1,
            Str2Long("10:00", "HH:mm"),
            Str2Long("11:40", "HH:mm"), 1, 1, 13
        ),
        LessonItem(
            5, "计算理论基础3下午", "田家炳教育书院236", 3,
            Str2Long("13:00", "HH:mm"),
            Str2Long("15:45", "HH:mm"), 2, 1, 17
        ),
        LessonItem(
            6, "计算理论基础5晚", "田家炳教育书院236", 5,
            Str2Long("18:00", "HH:mm"),
            Str2Long("19:40", "HH:mm"), 0, 1, 17
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

    fun getScheduleById(id: Int): LessonItem? {
        for (lesson in scheduleList) {
            if (id == lesson.id) {
                return lesson
            }
        }
        return null
    }
    //若出现时间冲突 则返回false，此处这一逻辑省略
    fun addLesson(lessonItem: LessonItem): Boolean {
        if (lessonItem.id >= 0) {
            val oldLesson = getScheduleById(lessonItem.id)
            if (oldLesson != null) {
                scheduleList.remove(oldLesson)
            }
            scheduleList.add(lessonItem)
        } else {
            lessonItem.id = id++
            scheduleList.add(lessonItem)
        }
        return true
    }

    fun deleteLesson(id: Int) {
        for (lesson in scheduleList) {
            if (id == lesson.id) {
                scheduleList.remove(lesson)
                break
            }
        }
    }

    private fun getWeekDaySchedule(query: Int): ArrayList<LessonItem> {
        val week = getWeekCount(startDate)
        val result = ArrayList<LessonItem>()
        for (schedule in scheduleList) {
            if (query == schedule.weekDay) {
                if (schedule.startWeek > week || schedule.endWeek < week) continue
                if (schedule.weekType == 0) {
                    // 没有单双周 判断在起始周范围内
                    result.add(schedule)
                } else if (schedule.weekType == 1 && week % 2 == 1L) {
                    // 单周 判断当前周是不是单周
                    result.add(schedule)
                } else if (schedule.weekType == 2 && week % 2 == 0L) {
                    // 双周
                    result.add(schedule)
                }
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