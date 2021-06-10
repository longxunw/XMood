package com.wlx.xmood.ui.schedule

import android.content.ContentValues
import android.database.CursorWindow
import android.database.sqlite.SQLiteCursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.liveData
import com.wlx.xmood.dao.MyDatabaseHelper
import com.wlx.xmood.ui.me.MeDataGet
import com.wlx.xmood.utils.ImageUtil
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.TimeUtil.getWeekCount
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object ScheduleDataGet {
    lateinit var dbHelper: MyDatabaseHelper
    private var id = 10
    private val TAG = "ScheduleDataGet"
    var startDate = "2021-03-01"
    var background = Drawable.createFromPath("src/main/res/drawable/all_lesson_background.jpg")
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
        scheduleList.clear()
        val db = dbHelper.writableDatabase
        val sql = "select * from Schedule order by startTime"
        val cursor = db.rawQuery(sql, null)
        cursor.apply {
            if (moveToFirst()) {
                Log.d(TAG, "getSchedule: movetofirst")
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
                } while (moveToNext())
            }
            close()
        }
        Log.d(TAG, "getSchedule: ${TimeUtil.Long2Str(scheduleList[0].startTime, "HH-mm")}")
        Log.d(TAG, "getSchedule: ${TimeUtil.Long2Str(scheduleList[0].endTime, "HH-mm")}")
        if (query == -1) {
            Result.success(scheduleList)
        } else {
            val week = getWeekCount(startDate)
            val result = ArrayList<LessonItem>()
            Log.d(TAG, "getWeekDaySchedule: ScheduleList size:${scheduleList.size}")
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
            Log.d(TAG, "getWeekDaySchedule: ${result.size}")
            Result.success(result)
        }
    }

    fun getScheduleById(id: Int): LessonItem {
        val db = dbHelper.writableDatabase
        val sql = "select * from Schedule where id = $id"
        val cursor = db.rawQuery(sql, null)
        var lessonItem: LessonItem? = null
        cursor.apply {
            if (cursor.moveToFirst()) {
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

    fun updateBackground(bg: Drawable): Boolean {
        background = bg
        return true
    }

    fun getBackground(id: Int) = fire(Dispatchers.IO) {
        val result = background
        if (result == null) {
            Result.success(null)
        } else {
            Result.success(result)
        }
    }

    //若出现时间冲突 则返回false，此处这一逻辑省略
    fun addLesson(lessonItem: LessonItem): Boolean {

        val db = dbHelper.writableDatabase
        scheduleList.clear()
        val sql = "select * from Schedule"
        val cursor = db.rawQuery(sql, null)
        cursor.apply {
            if (moveToFirst()) {
                Log.d(TAG, "getSchedule: movetofirst")
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
                } while (moveToNext())
            }
            close()
        }
        if (lessonItem.id == -1) {
            for (schedule in scheduleList) {
                // 不在同一天
                if (schedule.weekDay != lessonItem.weekDay) continue;
                var hasOverlappedWeek = false
                val scheduleWeeks = mutableSetOf<Int>()
                for (i in schedule.startWeek..schedule.endWeek) {
                    if (schedule.weekType == 1 && i % 2 == 1 ||
                        schedule.weekType == 2 && i % 2 == 0 ||
                        schedule.weekType == 0) {
                        scheduleWeeks.add(i)
                    }
                }

                for (i in lessonItem.startWeek..lessonItem.endWeek) {
                    if (scheduleWeeks.contains(i)) {
                        if (lessonItem.weekType == 1 && i % 2 == 1 ||
                            lessonItem.weekType == 2 && i % 2 == 0 ||
                            lessonItem.weekType == 0) {
                            hasOverlappedWeek = true
                            break
                        }
                    }
                }

                // 没有重合周数
                if (hasOverlappedWeek) {
                    if (schedule.startTime <= lessonItem.startTime && schedule.endTime > lessonItem.startTime) {
                        return false
                    }
                    if (lessonItem.startTime <= schedule.startTime && lessonItem.endTime > schedule.startTime) {
                        return false
                    }
                }
            }
        }
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

    fun getAllBgImg(): Bitmap {
        val db = dbHelper.writableDatabase
        val sql = "select lessonImg from User"
        val cursor = db.rawQuery(sql, null, null) as SQLiteCursor
        val cf = CursorWindow("cursorWindow", 1024 * 1024 * 30)
        cursor.window = cf
        lateinit var bitmap: Bitmap
        cursor.apply {
            if (moveToFirst()) {
                bitmap = ImageUtil.byteArray2Bitmap(getBlob(getColumnIndex("lessonImg")))
            }
            close()
        }
        return bitmap
    }

    fun setAllBgImg(bitmap: Bitmap) {
        val db = MeDataGet.dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("lessonImg", ImageUtil.bitmap2ByteArray(bitmap))
        }
        db.update("User", value, "", arrayOf())
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