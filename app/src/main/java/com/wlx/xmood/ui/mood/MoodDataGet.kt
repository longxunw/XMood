package com.wlx.xmood.ui.mood

import android.content.ContentValues
import androidx.lifecycle.liveData
import com.wlx.xmood.dao.MyDatabaseHelper
import com.wlx.xmood.utils.TimeUtil
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

object MoodDataGet {
    lateinit var dbHelper: MyDatabaseHelper

    var id = 666

    var allNodeList = arrayListOf<MoodChartItem>(
        MoodChartItem(0, TimeUtil.Str2Long("2021年5月25日 13:00","yyyy年MM月dd日 HH:mm"),
            1,"Android真的好难好难好难好难好难好难好难", "Happy,Sad"),
        MoodChartItem(1, TimeUtil.Str2Long("2021年5月26日 13:00","yyyy年MM月dd日 HH:mm"),
            3,"1111", "Happy,Sad"),
        MoodChartItem(2, TimeUtil.Str2Long("2021年5月27日 13:00","yyyy年MM月dd日 HH:mm"),
            3,"1111", "Happy,Sad"),
        MoodChartItem(3, TimeUtil.Str2Long("2021年5月28日 13:00","yyyy年MM月dd日 HH:mm"),
            4,"1111", "Happy,Sad"),
        MoodChartItem(4, TimeUtil.Str2Long("2021年5月29日 13:00","yyyy年MM月dd日 HH:mm"),
            2,"1111", "Happy,Sad"),
        MoodChartItem(5, TimeUtil.Str2Long("2021年5月30日 13:00","yyyy年MM月dd日 HH:mm"),
            6,"Android真的好难好难好难好难好难好难好难", "Happy,Sad")

    )

    val nodeList = ArrayList<MoodChartItem>()




    fun addNode(moodChartItem: MoodChartItem){
        allNodeList.add(moodChartItem)
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("date", moodChartItem.date)
            put("rating", moodChartItem.rating)
            put("event",moodChartItem.event)
            put("category", moodChartItem.category))
        }
        db.insert("Mood", null, value)
    }

    fun getAllChartNodes(query: Int) = fire(Dispatchers.IO){
        nodeList.clear()
        val db = dbHelper.writableDatabase
        val sql = "select * from Mood"
        val cursor = db.rawQuery(sql, null)
        cursor.apply {
            if(moveToFirst()) {
                do{
                    val item = MoodChartItem(
                        getInt(getColumnIndex("id")),
                        getLong(getColumnIndex("date")),
                        getInt(getColumnIndex("rating")),
                        getString(getColumnIndex("category")),
                        getString(getColumnIndex("event"))
                    )
                    nodeList.add(item)
                }while (moveToNext())
            }
        }
        Result.success(nodeList)
    }

    fun getChartNodeById(query: Int) = fire(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val sql = "select * from Mood where id = $id"
        val cursor = db.rawQuery(sql, null)
        val moodChartItem: MoodChartItem? = null
        cursor.apply {
            if(moveToFirst()) {
                moodChartItem = MoodChartItem(
                    getInt(getColumnIndex("id")),
                    getLong(getColumnIndex("date")),
                    getInt(getColumnIndex("rating")),
                    getString(getColumnIndex("category")),
                    getString(getColumnIndex("event"))
                )
            }
            close()
        }
        Result.success(moodChartItem)
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