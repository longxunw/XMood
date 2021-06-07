package com.wlx.xmood.ui.memorandum

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import androidx.lifecycle.liveData
import com.wlx.xmood.dao.MyDatabaseHelper
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

object MemoDataGet {
    val TAG = "MemoDataGet"
    var id = 10
    lateinit var dbHelper: MyDatabaseHelper

    //    var allnoteList = arrayListOf<MemorandumItem>(
//        MemorandumItem(
//            0,
//            "TCP与UDP", "TCP是有连接，UDP是无连接",
//            TimeUtil.Str2Date("2021年4月17日", "yyyy年MM月dd日"), "计网"
//        ),
//        MemorandumItem(
//            1,
//            "线程与进程", "进程可以拥有多个线程，进程是操作系统内存分配的基本单位",
//            TimeUtil.Str2Date("2021年4月24日", "yyyy年MM月dd日"), "OS"
//        ),
//        MemorandumItem(
//            2,
//            "线程与协程", "协程比线程更小，可以用来执行轻量级任务",
//            TimeUtil.Str2Date("2021年5月4日", "yyyy年MM月dd日"), "Android"
//        ),
//        MemorandumItem(
//            3,
//            "项目deadline", "6.11",
//            TimeUtil.Str2Date("2021年5月25日", "yyyy年MM月dd日"), "Android"
//        )
//    )
//    private val noteList = ArrayList<MemorandumItem>().apply {
//        addAll(allnoteList)
//    }
    private val noteList = ArrayList<MemorandumItem>()

    //解决莫名奇妙的双重数据问题
    private val noteIdList = ArrayList<Int>()
    var mcatalog = "__all"
    var navTitleList = arrayListOf<String>(
        "默认分类",
        "计网",
        "OS",
        "Android"
    )

//       fun deleteNote(id: Int) {
//        for (memo in allnoteList) {
//            if (id == memo.id) {
//                allnoteList.remove(memo)
//                break
//            }
//        }
//    }
    fun deleteNote(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("Note", "id = ?", arrayOf(id.toString()))
    }

    //    fun getNoteById(id: Int): MemorandumItem? {
//        for (memo in allnoteList) {
//            if (id == memo.id) {
//                return memo
//            }
//        }
//        return null
//    }
    fun getNoteById(id: Int): MemorandumItem? {
        val db = dbHelper.writableDatabase
        val sql = "select * from Note where id = $id"
        val cursor = db.rawQuery(sql, null)
        var memorandumItem: MemorandumItem? = null
        cursor.apply {
            if (cursor.moveToFirst()) {
                memorandumItem = MemorandumItem(
                    getInt(getColumnIndex("id")),
                    getString(getColumnIndex("head")),
                    getString(getColumnIndex("body")),
                    Date(getLong(getColumnIndex("updateTime"))),
                    getString(getColumnIndex("catalog"))
                )
            }
            close()
        }
        return memorandumItem
    }

    //    private fun changeToCatalog(catalog: String) {
//        mcatalog = catalog
//        noteList.clear()
//        for (memo in allnoteList) {
//            if (catalog == memo.catalog) {
//                noteList.add(memo)
//            }
//        }
//    }
    private fun changeToCatalog(catalog: String) {
        val db = dbHelper.writableDatabase
        lateinit var cursor: Cursor
        if (catalog == "__all") {
            mcatalog = "__all"
            val sql = "select * from Note order by updateTime"
            cursor = db.rawQuery(sql, null)
        } else {
            Log.d(TAG, "changeToCatalog: $catalog")
            mcatalog = catalog
            val sql = "select * from Note where catalog = \"$catalog\" order by updateTime"
            cursor = db.rawQuery(sql, null)
        }
        cursor.apply {
            if (moveToFirst()) {
                do {
                    val item = MemorandumItem(
                        getInt(getColumnIndex("id")),
                        getString(getColumnIndex("head")),
                        getString(getColumnIndex("body")),
                        Date(getLong(getColumnIndex("updateTime"))),
                        getString(getColumnIndex("catalog"))
                    )
//                    if (!noteIdList.contains(item.id)) {
                    noteList.add(item)
                    Log.d(TAG, "changeToCatalog: ${item.head}")
//                        noteIdList.add(item.id)
//                    }
                } while (moveToNext())
            }
            close()
        }
    }

    fun getNote(query: String) = fire(Dispatchers.IO) {
        noteList.clear()
//        noteIdList.clear()
        changeToCatalog(query)
        Log.d(TAG, "getNote: $query, size: ${noteList.size}")
        Result.success(noteList)
    }

    //    fun addNote(memorandumItem: MemorandumItem) {
//        if (memorandumItem.catalog.isEmpty()) {
//            memorandumItem.catalog = "默认分类"
//        }
//        if (memorandumItem.id == -1) {
//            memorandumItem.id = id++
//            allnoteList.add(memorandumItem)
//        } else {
//            for (memo in allnoteList) {
//                if (memorandumItem.id == memo.id) {
//                    allnoteList.remove(memo)
//                    break
//                }
//            }
//            allnoteList.add(memorandumItem)
//        }
//    }
    fun addNote(memorandumItem: MemorandumItem) {
        if (memorandumItem.catalog.isEmpty()) {
            memorandumItem.catalog = "默认分类"
        }
        Log.d(TAG, "addNote: 111")
        val db = dbHelper.writableDatabase
        if (memorandumItem.id == -1) {
            val value = ContentValues().apply {
                put("head", memorandumItem.head)
                put("updateTime", memorandumItem.updateTime.time)
                put("catalog", memorandumItem.catalog)
                put("body", memorandumItem.body)
            }
            Log.d(TAG, "addNote: add")
            db.insert("Note", null, value)
        } else {
            val value = ContentValues().apply {
                put("head", memorandumItem.head)
                put("updateTime", memorandumItem.updateTime.time)
                put("catalog", memorandumItem.catalog)
                put("body", memorandumItem.body)
            }
            Log.d(TAG, "addNote: update")
            db.update("Note", value, "id = ?", arrayOf(memorandumItem.id.toString()))
        }
    }

    //    fun getNote(query: String) = fire(Dispatchers.IO) {
//        if (query == "__all") {
//            Log.d(TAG, "getNote: allNoteList")
//            Result.success(allnoteList)
//        } else {
//            changeToCatalog(query)
//            Log.d(TAG, "getNote: $query")
//            Result.success(noteList)
//        }
//    }


    //    fun getNavTitle(query: String) = fire(Dispatchers.IO) {
//        val result = navTitleList
//        if (result == null) {
//            Result.success(null)
//        } else {
//            Result.success(result)
//        }
//    }
    fun getNavTitle(query: String) = fire(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val sql = "select distinct catalog from Note order by catalog"
        val cursor = db.rawQuery(sql, null)
        navTitleList.clear()
        cursor.apply {
            if (moveToFirst()) {
                do {
                    navTitleList.add(getString(getColumnIndex("catalog")))
                    Log.d(TAG, "getNavTitle: ${getString(getColumnIndex("catalog"))}")
                } while (moveToNext())
            }
            close()
        }
        Log.d(TAG, "getNavTitle: size ${navTitleList.size}")
        Result.success(navTitleList)
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