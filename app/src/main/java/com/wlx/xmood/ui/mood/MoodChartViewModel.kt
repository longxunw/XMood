package com.wlx.xmood.ui.mood

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MoodChartViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val searchNodeLiveData = MutableLiveData<Int>()

    val nodeList = ArrayList<MoodChartItem>()
//    val nodeRateList = ArrayList<Int>()

    val nodeLiveData = Transformations.switchMap(searchNodeLiveData){ query ->
        MoodDataGet.getAllChartNodes(query)
    }

    fun searchNode(query: Int){
        searchNodeLiveData.value = query
    }
}