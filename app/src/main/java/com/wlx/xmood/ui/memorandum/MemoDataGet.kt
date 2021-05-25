package com.wlx.xmood.ui.memorandum

import android.util.Log
import androidx.lifecycle.liveData
import com.wlx.xmood.utils.TimeUtil
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object MemoDataGet {
    val TAG = "MemoDataGet"
    var id = 10
    var allnoteList = arrayListOf<MemorandumItem>(
        MemorandumItem(
            0,
            "TCP与UDP", "TCP是有连接，UDP是无连接",
            TimeUtil.Str2Date("2021年4月17日", "yyyy年MM月dd日"), "计网"
        ),
        MemorandumItem(
            1,
            "线程与进程", "进程可以拥有多个线程，进程是操作系统内存分配的基本单位",
            TimeUtil.Str2Date("2021年4月24日", "yyyy年MM月dd日"), "OS"
        ),
        MemorandumItem(
            2,
            "线程与协程", "协程比线程更小，可以用来执行轻量级任务",
            TimeUtil.Str2Date("2021年5月4日", "yyyy年MM月dd日"), "Android"
        ),
        MemorandumItem(
            3,
            "项目deadline", "6.11",
            TimeUtil.Str2Date("2021年5月25日", "yyyy年MM月dd日"), "Android"
        )
    )
    val noteList = ArrayList<MemorandumItem>().apply {
        addAll(allnoteList)
    }
    var mcatalog = "__all"
    var navTitleList = arrayListOf<String>(
        "默认分类",
        "计网",
        "OS",
        "Android"
    )

    fun deleteNote(id: Int) {
        for (memo in allnoteList) {
            if (id == memo.id) {
                allnoteList.remove(memo)
                break
            }
        }
    }

    fun getNoteById(id: Int): MemorandumItem? {
        for (memo in allnoteList) {
            if (id == memo.id) {
                return memo
            }
        }
        return null
    }

    private fun changeToCatalog(catalog: String) {
        mcatalog = catalog
        noteList.clear()
        for (memo in allnoteList) {
            if (catalog == memo.catalog) {
                noteList.add(memo)
            }
        }
    }

    fun addNote(memorandumItem: MemorandumItem) {
        if (memorandumItem.catalog.isEmpty()) {
            memorandumItem.catalog = "默认分类"
        }
        if (memorandumItem.id == -1) {
            memorandumItem.id = id++
            allnoteList.add(memorandumItem)
        } else {
            for (memo in allnoteList) {
                if (memorandumItem.id == memo.id) {
                    allnoteList.remove(memo)
                    break
                }
            }
            allnoteList.add(memorandumItem)
        }
    }

    fun getNote(query: String) = fire(Dispatchers.IO) {
        if (query == "__all") {
            Log.d(TAG, "getNote: allNoteList")
            Result.success(allnoteList)
        } else {
            changeToCatalog(query)
            Log.d(TAG, "getNote: $query")
            Result.success(noteList)
        }
    }


    fun getNavTitle(query: String) = fire(Dispatchers.IO) {
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