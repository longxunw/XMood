package com.wlx.xmood.ui.daily

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.liveData
import com.wlx.xmood.dao.MyDatabaseHelper
import com.wlx.xmood.utils.TimeUtil
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

// 仅为测试用，编写数据库模块后使用数据库模块
object DailyDataGet {
    private var id = 4
    private val TAG = "DailyDataGet"
    lateinit var dbHelper: MyDatabaseHelper
//    val dailyEvents = mutableMapOf<String, ArrayList<DailyItem>>(
//        "2021-05-21" to arrayListOf(
//            DailyItem(
//                0,
//                TimeUtil.Str2Long("2021-05-21", "yyyy-MM-dd"),
//                TimeUtil.Str2Long("12:50", "HH:mm"),
//                TimeUtil.Str2Long("15:00", "HH:mm"),
//                "完成项目汇报", false, 0L, true,
//            ), DailyItem(
//                1,
//                TimeUtil.Str2Long("2021-05-21", "yyyy-MM-dd"),
//                TimeUtil.Str2Long("12:50", "HH:mm"),
//                TimeUtil.Str2Long("15:00", "HH:mm"),
//                "完成计算机理论基础作业", false, 0L, false
//            )
//        ),
//        "2021-05-10" to arrayListOf(
//            DailyItem(
//                2,
//                TimeUtil.Str2Long("2021-05-10", "yyyy-MM-dd"),
//                TimeUtil.Str2Long("14:00", "HH:mm"),
//                TimeUtil.Str2Long("15:00", "HH:mm"),
//                "完成项目展示ppt", false, 0L, false
//            )
//        )
//    )

    fun getEvent(string: String) = fire(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val dayLong = TimeUtil.Str2Long(string,"yyyy-MM-dd")
        val sql = "select * from Daily where day >= $dayLong and day < ${dayLong+1000*60*60*24} order by startTime"
        val cursor = db.rawQuery(sql,null)
        val result = ArrayList<DailyItem>()
        cursor.apply {
            if(moveToFirst()){
                do{
                    val item = DailyItem(
                        getInt(getColumnIndex("id")),
                        getLong(getColumnIndex("day")),
                        getLong(getColumnIndex("startTime")),
                        getLong(getColumnIndex("endTime")),
                        getString(getColumnIndex("event")),
                        getInt(getColumnIndex("isAlarm")) != 0,
                        getLong(getColumnIndex("alarmTime")),
                        getInt(getColumnIndex("isFinish")) != 0,
                    )
                    result.add(item)
                } while (moveToNext())
            }
        }
        Log.d(TAG, "getEvent: ${result.size}")
        Result.success(result)
    }

    fun getEventDay(string: String) = fire(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val sql = "select distinct day from Daily"
        val cursor = db.rawQuery(sql, null)
        val result = ArrayList<String>()
        cursor.apply {
            if(moveToFirst()){
                do{
                    result.add(TimeUtil.Long2Str(getLong(getColumnIndex("day")),"yyyy-MM-dd"))
                }while(moveToNext())
            }
        }
        Result.success(result)
    }

    fun getEventDayById(id: Int): DailyItem {
        val db = dbHelper.writableDatabase
        val sql = "select * from Daily where id = $id"
        val cursor = db.rawQuery(sql,null)
        var dailyItem: DailyItem? = null
        cursor.apply {
            if(cursor.moveToFirst()){
                dailyItem = DailyItem(
                    getInt(getColumnIndex("id")),
                    getLong(getColumnIndex("day")),
                    getLong(getColumnIndex("startTime")),
                    getLong(getColumnIndex("endTime")),
                    getString(getColumnIndex("event")),
                    getInt(getColumnIndex("isAlarm")) != 0,
                    getLong(getColumnIndex("alarmTime")),
                    getInt(getColumnIndex("isFinish")) != 0,
                )
            }
            close()
        }
        return dailyItem!!
    }

    //若出现时间冲突 则返回false，此处这一逻辑省略
    fun addDaily(dailyItem: DailyItem): Boolean {
        Log.e(TAG, "addDaily: " + TimeUtil.Long2Str(dailyItem.day, "yyyy-MM-dd"))
        val value = ContentValues().apply {
            put("day", dailyItem.day)
            put("startTime", dailyItem.startTime)
            put("endTime", dailyItem.endTime)
            put("event", dailyItem.event)
            put("isAlarm", dailyItem.isAlarm)
            put("alarmTime", dailyItem.alarmTime)
        }
        val db = dbHelper.writableDatabase
        if (dailyItem.id >= 0) {
            db.update("Daily",value,"id = ?", arrayOf(dailyItem.id.toString()))
        } else {
            db.insert("Daily",null,value)
        }
        return true
    }

    fun deleteDaily(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("Daily","id = ?", arrayOf(id.toString()))
    }

    fun dailyFinish(id: Int, boolean: Boolean) {
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("isFinish", boolean)
        }
        db.update("Daily", value, "id = ?", arrayOf(id.toString()))
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