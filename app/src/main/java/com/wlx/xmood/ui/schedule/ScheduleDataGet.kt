package com.wlx.xmood.ui.schedule

import android.content.ContentValues
import androidx.lifecycle.liveData
import com.wlx.xmood.dao.MyDatabaseHelper
import com.wlx.xmood.utils.TimeUtil.LongToDayLong
import com.wlx.xmood.utils.TimeUtil.Str2Long
import com.wlx.xmood.utils.TimeUtil.getWeekCount
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object ScheduleDataGet {
    lateinit var dbHelper: MyDatabaseHelper
    private var id = 10
    var startDate = "2021-03-01"
    val scheduleList = arrayListOf<LessonItem>(
//        LessonItem(
//            0, "计算理论基础1", "田家炳教育书院236", 1,
//            LongToDayLong(Str2Long("08:00", "HH:mm")),
//            LongToDayLong(Str2Long("09:30", "HH:mm")),
//            0, 1, 17
//        ),
//        LessonItem(
//            1, "计算理论基础2双周", "田家炳教育书院236", 2,
//            LongToDayLong(Str2Long("10:00", "HH:mm")),
//            LongToDayLong(Str2Long("11:30", "HH:mm")),
//            2, 1, 17
//        ),
//        LessonItem(
//            8, "计算理论基础2单周", "田家炳教育书院236", 2,
//            LongToDayLong(Str2Long("13:00", "HH:mm")),
//            LongToDayLong(Str2Long("14:40", "HH:mm")),
//            1, 1, 17
//        ),
//        LessonItem(
//            2, "计算理论基础5", "田家炳教育书院236", 5,
//            LongToDayLong(Str2Long("08:00", "HH:mm")),
//            LongToDayLong(Str2Long("10:45", "HH:mm")),
//            2, 11, 17
//        ),
//        LessonItem(
//            3, "计算理论基础3上午", "田家炳教育书院236", 3,
//            LongToDayLong(Str2Long("10:00", "HH:mm")),
//            LongToDayLong(Str2Long("11:40", "HH:mm")),
//            0, 13, 17
//        ),
//        LessonItem(
//            4, "计算理论基础1上午", "田家炳教育书院236", 1,
//            LongToDayLong(Str2Long("10:00", "HH:mm")),
//            LongToDayLong(Str2Long("11:40", "HH:mm")),
//            1, 1, 13
//        ),
//        LessonItem(
//            5, "计算理论基础3下午", "田家炳教育书院236", 3,
//            LongToDayLong(Str2Long("13:00", "HH:mm")),
//            LongToDayLong(Str2Long("15:45", "HH:mm")),
//            2, 1, 17
//        ),
//        LessonItem(
//            6, "计算理论基础5晚", "田家炳教育书院236", 5,
//            LongToDayLong(Str2Long("18:00", "HH:mm")),
//            LongToDayLong(Str2Long("19:40", "HH:mm")),
//            0, 1, 17
//        )
    )

    fun getSchedule(query: Int) = fire(Dispatchers.IO) {
        if (query == -1) {
            scheduleList.clear()
            val db = dbHelper.writableDatabase
            val sql = "select * from Schdule"
            val cursor = db.rawQuery(sql, null)
            cursor.apply {
                if(moveToFirst()) {
                    do {
                        val item = LessonItem(
                            getInt(getColumnIndex("id")),
                            getString(getColumnIndex("name")),
                            getString(getColumnIndex("location")),
                            getInt(getColumnIndex("weekDay")),
                            getLong(getColumnIndex("startTime")),
                            getLong(getColumnIndex("endTime")),
                            getInt(getColumnIndex("weekType")),
                            getInt(getColumnIndex("startWeek")),
                            getInt(getColumnIndex("endWeek"))
                        )
                        scheduleList.add(item)
                    } while(moveToNext())
                }
            }
            Result.success(scheduleList)
        } else {
            val result = getWeekDaySchedule(query)
            Result.success(result)
        }
    }

    fun getScheduleById(id: Int): LessonItem {
        val db = dbHelper.writableDatabase
        val sql = "select * from Schedule where id = $id"
        val cursor = db.rawQuery(sql, null)
        var lessonItem: LessonItem? = null
        cursor.apply {
            if(cursor.moveToFirst()) {
                lessonItem = LessonItem(
                    getInt(getColumnIndex("id")),
                    getString(getColumnIndex("name")),
                    getString(getColumnIndex("location")),
                    getInt(getColumnIndex("weekDay")),
                    getLong(getColumnIndex("startTime")),
                    getLong(getColumnIndex("endTime")),
                    getInt(getColumnIndex("weekType")),
                    getInt(getColumnIndex("startWeek")),
                    getInt(getColumnIndex("endWeek"))
                )
            }
            close()
        }
        return lessonItem!!
    }

    //若出现时间冲突 则返回false，此处这一逻辑省略
    fun addLesson(lessonItem: LessonItem): Boolean {
        val value = ContentValues().apply {
            put("name", lessonItem.name)
            put("location", lessonItem.location)
            put("weekDay", lessonItem.weekDay)
            put("startTime", lessonItem.startTime)
            put("endTime", lessonItem.endTime)
            put("weekType", lessonItem.weekType)
            put("startWeek", lessonItem.startWeek)
            put("endWeek", lessonItem.endWeek)
        }
        val db = dbHelper.writableDatabase

        if (lessonItem.id >= 0) {
            db.update("Schedule", value, "id = ?", arrayOf(lessonItem.id.toString()))
        } else {
            db.insert("Schedule", null, value)
        }
        return true
    }

    fun deleteLesson(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("Schedule", "id = ?", arrayOf(id.toString()))
    }

    private fun getWeekDaySchedule(query: Int): ArrayList<LessonItem> {
        getSchedule(-1)
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