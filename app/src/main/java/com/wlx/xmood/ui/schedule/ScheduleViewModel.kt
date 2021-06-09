package com.wlx.xmood.ui.schedule

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class ScheduleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is schedule Fragment"
    }
    val text: LiveData<String> = _text

    private val searchScheduleLiveData = MutableLiveData<Int>()

    val scheduleList = ArrayList<LessonItem>()

    val scheduleLiveData = Transformations.switchMap(searchScheduleLiveData) { query ->
        ScheduleDataGet.getSchedule(query)
    }

    fun searchSchedule(query: Int) {
        searchScheduleLiveData.value = query
    }

}