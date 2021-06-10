package com.wlx.xmood.ui.mood

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MoodChartViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val searchNodeLiveData = MutableLiveData<Pair<Long, Long>>()

    val nodeList = ArrayList<MoodChartItem>()
//    val nodeRateList = ArrayList<Int>()

    val nodeLiveData = Transformations.switchMap(searchNodeLiveData) { query ->
//        MoodDataGet.getAllChartNodes(query)
        MoodDataGet.getTimeTypeCharNode(query.first, query.second)
    }

    fun searchNode(startTime: Long, endTime: Long) {
        searchNodeLiveData.value = Pair(startTime, endTime)
    }
}