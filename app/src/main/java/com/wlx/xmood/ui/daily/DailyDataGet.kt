package com.wlx.xmood.ui.daily

import androidx.lifecycle.liveData
import com.wlx.xmood.utils.TimeUtil
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

// 仅为测试用，编写数据库模块后使用数据库模块
object DailyDataGet {
    val dailyEvents = mapOf<String, ArrayList<DailyItem>>(
        "2021-05-21" to arrayListOf(
            DailyItem(
                1,
            TimeUtil.Str2Long("2021-05-21", "yyyy-MM-dd"),
                TimeUtil.Str2Long("12:50", "HH:mm"),
                TimeUtil.Str2Long("15:00", "HH:mm"),
                "完成项目汇报", false, true
            ), DailyItem(
                1,
                TimeUtil.Str2Long("2021-05-21", "yyyy-MM-dd"),
                TimeUtil.Str2Long("12:50", "HH:mm"),
                TimeUtil.Str2Long("15:00", "HH:mm"),
                "完成计算机理论基础作业", false, false
            )
        ),
        "2021-05-10" to arrayListOf(
            DailyItem(
                2,
                TimeUtil.Str2Long("2021-05-10", "yyyy-MM-dd"),
                TimeUtil.Str2Long("14:00", "HH:mm"),
                TimeUtil.Str2Long("15:00", "HH:mm"),
                "完成项目展示ppt", false, false
            )
        )
    )

    fun getEvent(string: String) = fire(Dispatchers.IO) {
        val result = dailyEvents[string]
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