package com.wlx.xmood.ui.mood

import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.liveData
import com.wlx.xmood.ui.memorandum.MemoDataGet
import com.wlx.xmood.utils.TimeUtil
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Collections.addAll
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

object MoodDataGet {

    var id = 666

    var allNodeList = arrayListOf<MoodChartItem>(
        MoodChartItem(0, TimeUtil.Str2Date("2021年5月25日 13:00","yyyy年MM月dd日 HH:mm"),
            1,"1111", arrayOf<String>("Happy","Sad")),
        MoodChartItem(1, TimeUtil.Str2Date("2021年5月26日 13:00","yyyy年MM月dd日 HH:mm"),
            2,"1111", arrayOf<String>("Happy","Sad")),
        MoodChartItem(2, TimeUtil.Str2Date("2021年5月27日 13:00","yyyy年MM月dd日 HH:mm"),
            3,"1111", arrayOf<String>("Happy","Sad")),
        MoodChartItem(3, TimeUtil.Str2Date("2021年5月28日 13:00","yyyy年MM月dd日 HH:mm"),
            4,"1111", arrayOf<String>("Happy","Sad")),
        MoodChartItem(4, TimeUtil.Str2Date("2021年5月29日 13:00","yyyy年MM月dd日 HH:mm"),
            5,"1111", arrayOf<String>("Happy","Sad")),
        MoodChartItem(5, TimeUtil.Str2Date("2021年5月30日 13:00","yyyy年MM月dd日 HH:mm"),
            6,"1111", arrayOf<String>("Happy","Sad")),

    )

    val nodeList = ArrayList<MoodChartItem>().apply {
        addAll(allNodeList)
    }


    fun addNode(moodChartItem: MoodChartItem){
        allNodeList.add(moodChartItem)
    }

    fun getChartNode(query: Int) = fire(Dispatchers.IO){
        Result.success(allNodeList)

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