package com.wlx.xmood.ui.memorandum

import androidx.lifecycle.liveData
import com.wlx.xmood.utils.TimeUtil
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object MemoDataGet {
    val noteList = arrayListOf<MemorandumItem>(
        MemorandumItem(
            "TCP与UDP", "TCP是有连接，UDP是无连接",
            TimeUtil.Str2Date("2021年4月17日", "yyyy年MM月dd日"), "计网"
        ),
        MemorandumItem(
            "线程与进程", "进程可以拥有多个线程，进程是操作系统内存分配的基本单位",
            TimeUtil.Str2Date("2021年4月24日", "yyyy年MM月dd日"), "OS"
        ),
        MemorandumItem(
            "线程与协程", "协程比线程更小，可以用来执行轻量级任务",
            TimeUtil.Str2Date("2021年5月4日", "yyyy年MM月dd日"), "Android"
        )
    )
    var allnoteList = arrayListOf<MemorandumItem>(
        MemorandumItem(
            "TCP与UDP", "TCP是有连接，UDP是无连接",
            TimeUtil.Str2Date("2021年4月17日", "yyyy年MM月dd日"), "计网"
        ),
        MemorandumItem(
            "线程与进程", "进程可以拥有多个线程，进程是操作系统内存分配的基本单位",
            TimeUtil.Str2Date("2021年4月24日", "yyyy年MM月dd日"), "OS"
        ),
        MemorandumItem(
            "线程与协程", "协程比线程更小，可以用来执行轻量级任务",
            TimeUtil.Str2Date("2021年5月4日", "yyyy年MM月dd日"), "Android"
        )
    )
    var navTitleList = arrayListOf<String>(
        "计网",
        "OS",
        "Android"
    )

    fun getNote() = fire(Dispatchers.IO) {
        val result = noteList
        if (result == null) {
            Result.success(null)
        } else {
            Result.success(result)
        }
    }

    fun getAllnote() = fire(Dispatchers.IO) {
        val result = allnoteList
        if (result == null) {
            Result.success(null)
        } else {
            Result.success(result)
        }
    }

    fun getNavTitle() = fire(Dispatchers.IO) {
        val result = navTitleList
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