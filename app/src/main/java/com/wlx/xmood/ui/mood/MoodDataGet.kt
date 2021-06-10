package com.wlx.xmood.ui.mood

import android.content.ContentValues
import androidx.lifecycle.liveData
import com.wlx.xmood.dao.MyDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object MoodDataGet {
    lateinit var dbHelper: MyDatabaseHelper

//    var id = 666

//    var allNodeList = arrayListOf<MoodChartItem>(
//        MoodChartItem(0, TimeUtil.Str2Long("2021年6月7日 13:00","yyyy年MM月dd日 HH:mm"),
//            1,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry"),
//        MoodChartItem(1, TimeUtil.Str2Long("2021年6月8日 13:09","yyyy年MM月dd日 HH:mm"),
//            3,"1111", "Joyful,Angry"),
//        MoodChartItem(2, TimeUtil.Str2Long("2021年6月8日 13:09","yyyy年MM月dd日 HH:mm"),
//            3,"1111", "Joyful,Angry"),
//        MoodChartItem(3, TimeUtil.Str2Long("2021年6月9日 13:00","yyyy年MM月dd日 HH:mm"),
//            4,"1111", "Joyful,Angry"),
//        MoodChartItem(4, TimeUtil.Str2Long("2021年6月9日 13:00","yyyy年MM月dd日 HH:mm"),
//            2,"1111", "Joyful,Angry"),
//
//        MoodChartItem(5, TimeUtil.Str2Long("2021年6月10日 4:00","yyyy年MM月dd日 HH:mm"),
//            1,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry"),
//        MoodChartItem(6, TimeUtil.Str2Long("2021年6月10日 5:00","yyyy年MM月dd日 HH:mm"),
//            3,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry"),
//        MoodChartItem(7, TimeUtil.Str2Long("2021年6月10日 6:00","yyyy年MM月dd日 HH:mm"),
//            2,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry"),
//        MoodChartItem(8, TimeUtil.Str2Long("2021年6月10日 7:00","yyyy年MM月dd日 HH:mm"),
//            4,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry"),
//        MoodChartItem(9, TimeUtil.Str2Long("2021年6月10日 12:10","yyyy年MM月dd日 HH:mm"),
//            5,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry"),
//        MoodChartItem(10, TimeUtil.Str2Long("2021年6月10日 12:23","yyyy年MM月dd日 HH:mm"),
//            6,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry"),
//        MoodChartItem(11, TimeUtil.Str2Long("2021年6月10日 12:31","yyyy年MM月dd日 HH:mm"),
//            4,"Android真的好难好难好难好难好难好难好难", "Joyful,Angry")
//
//    )

    val nodeList = ArrayList<MoodChartItem>()


    fun addNode(moodChartItem: MoodChartItem) {
//        allNodeList.add(moodChartItem)
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("date", moodChartItem.date)
            put("rating", moodChartItem.rating)
            put("event",moodChartItem.event)
            put("category", moodChartItem.category)
        }
        db.insert("Mood", null, value)
    }

    fun getAllChartNodes(query: Int) = fire(Dispatchers.IO){
//        Result.success(allNodeList)
        // Just for test --- with mock data

        nodeList.clear()
        val db = dbHelper.writableDatabase
        val sql = "select * from Mood"
        val cursor = db.rawQuery(sql, null)
        cursor.apply {
            if (moveToFirst()) {
                do {
                    val item = MoodChartItem(
                        getInt(getColumnIndex("id")),
                        getLong(getColumnIndex("date")),
                        getInt(getColumnIndex("rating")),
                        getString(getColumnIndex("event")),
                        getString(getColumnIndex("category"))
                    )
                    nodeList.add(item)
                } while (moveToNext())
            }
        }
        Result.success(nodeList)
    }

    fun getChartNodeById(query: Int) : MoodChartItem? {
        val db = dbHelper.writableDatabase
        val sql = "select * from Mood where id = $query"
        val cursor = db.rawQuery(sql, null)
        var moodChartItem: MoodChartItem? = null
        cursor.apply {
            if (moveToFirst()) {
                moodChartItem = MoodChartItem(
                    getInt(getColumnIndex("id")),
                    getLong(getColumnIndex("date")),
                    getInt(getColumnIndex("rating")),
                    getString(getColumnIndex("event")),
                    getString(getColumnIndex("category"))
                )
            }
            close()
        }
        return moodChartItem!!

        //Just for test --- with mock data
//        for (node in allNodeList) {
//            if (query == node.id) {
//                return node
//            }
//        }
//        return null
    }

    fun getTimeTypeCharNode(startTime: Long, endTime: Long) = fire(Dispatchers.IO) {
        val result = ArrayList<MoodChartItem>()
        val db = dbHelper.writableDatabase
        var sql = "select * from Mood where date >= $startTime and date < $endTime"
        val cursor = db.rawQuery(sql, null, null)
        cursor.apply {
            if (moveToFirst()) {
                do {
                    val item = MoodChartItem(
                        getInt(getColumnIndex("id")),
                        getLong(getColumnIndex("date")),
                        getInt(getColumnIndex("rating")),
                        getString(getColumnIndex("event")),
                        getString(getColumnIndex("category"))
                    )
                    result.add(item)
                } while (moveToNext())
            }
            close()
        }
        Result.success(result)
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