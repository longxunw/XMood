package com.wlx.xmood.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class LessonAllViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<Int>()

    val scheduleList = ArrayList<LessonItem>()

    val scheduleLiveData = Transformations.switchMap(searchLiveData) { query ->
        ScheduleDataGet.getSchedule(query)
    }

    fun searchSchedule(query: Int) {
        searchLiveData.value = query
    }
}