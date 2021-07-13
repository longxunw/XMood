package com.wlx.xmood.ui.countDown

import android.content.ContentValues
import androidx.lifecycle.liveData
import com.wlx.xmood.dao.MyDatabaseHelper
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

/**
 * @author  : wanglongxun
 * @date    : 2021/7/13 10:32
 */
object CountDownDataGet {
    val TAG = "CountDownDataGet"
    lateinit var dbHelper: MyDatabaseHelper

    fun getAll() = fire(Dispatchers.IO){
        val db = dbHelper.writableDatabase
        val sql = "select * from Countdown"
        val cursor = db.rawQuery(sql,null)
        val result = ArrayList<CountDownItem>()
        cursor.apply {
            if(moveToFirst()){
                do {
                    val item = CountDownItem(
                        getInt(getColumnIndex("id")),
                        getString(getColumnIndex("content")),
                        getLong(getColumnIndex("startDay"))
                    )
                }while(moveToNext())
            }
        }
        Result.success(result)
    }

    fun addCountDown(countDownItem: CountDownItem){
        val value = ContentValues().apply {
            put("content",countDownItem.content)
            put("startDay",countDownItem.startDay)
        }
        val db = dbHelper.writableDatabase
        if(countDownItem.id >= 0){
            db.update("Countdown",value,"id = ?", arrayOf(countDownItem.id.toString()))
        }else{
            db.insert("Countdown",null,value)
        }
    }

    fun deleteCountDown(id: Int){
        val db = dbHelper.writableDatabase
        db.delete("CountDown","id = ?", arrayOf(id.toString()))
    }

    fun getCountDownById(id: Int) : CountDownItem{
        val db = dbHelper.writableDatabase
        val sql = "select * from Countdown where id = $id"
        val cursor = db.rawQuery(sql,null)
        var countDownItem : CountDownItem? = null
        cursor.apply {
            if(cursor.moveToFirst()){
                countDownItem = CountDownItem(
                    getInt(getColumnIndex("id")),
                    getString(getColumnIndex("content")),
                    getLong(getColumnIndex("startDay"))
                )
            }
            close()
        }
        return countDownItem!!
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