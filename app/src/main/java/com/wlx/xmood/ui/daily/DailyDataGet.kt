package com.wlx.xmood.ui.daily

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
    val dailyEvents = mutableMapOf<String, ArrayList<DailyItem>>(
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
    )

    fun getEvent(string: String) = fire(Dispatchers.IO) {
        val result = dailyEvents[string]
        if (result == null) {
            Result.success(null)
        } else {
            Result.success(result)
        }
    }

    fun getEventDay(string: String) = fire(Dispatchers.IO) {
        val result = dailyEvents.keys
        if (result == null) {
            Result.success(null)
        } else {
            Result.success(result)
        }
    }

    fun getEventDayById(id: Int): DailyItem {
        for ((key, value) in dailyEvents) {
            for (daily in value) {
                if (daily.id == id) {
                    return daily
                }
            }
        }
        return DailyItem(
            2,
            TimeUtil.Str2Long("2021-05-10", "yyyy-MM-dd"),
            TimeUtil.Str2Long("14:00", "HH:mm"),
            TimeUtil.Str2Long("15:00", "HH:mm"),
            "完成项目展示ppt", false, 0L, false
        )
    }

    //若出现时间冲突 则返回false，此处这一逻辑省略
    fun addDaily(dailyItem: DailyItem): Boolean {
        Log.e(TAG, "addDaily: " + TimeUtil.Long2Str(dailyItem.day, "yyyy-MM-dd"))
        if (dailyItem.id >= 0) {
            val oldDaily = getEventDayById(dailyItem.id)
            if (oldDaily != null) {
                dailyEvents[TimeUtil.Long2Str(oldDaily.day, "yyyy-MM-dd")]?.remove(oldDaily)
            }
        } else {
            dailyItem.id = id++
        }
        if (dailyEvents[TimeUtil.Long2Str(dailyItem.day, "yyyy-MM-dd")] == null) {
            dailyEvents[TimeUtil.Long2Str(dailyItem.day, "yyyy-MM-dd")] = arrayListOf<DailyItem>(dailyItem)
        } else {
            dailyEvents[TimeUtil.Long2Str(dailyItem.day, "yyyy-MM-dd")]?.add(dailyItem)
        }
        Log.e(TAG, "addDaily: " + dailyEvents[TimeUtil.Long2Str(dailyItem.day, "yyyy-MM-dd")]?.size)
        Log.e(TAG, "addDaily: " + getEventDayById(dailyItem.id)?.isFinish)
        return true
    }

    fun deleteDaily(id: Int) {
        for ((key, value) in dailyEvents) {
            for (daily in value) {
                if (id == daily.id) {
                    dailyEvents[key]?.remove(daily)
                    break
                }
            }
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