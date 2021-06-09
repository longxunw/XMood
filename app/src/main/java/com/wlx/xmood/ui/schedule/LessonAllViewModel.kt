package com.wlx.xmood.ui.schedule

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.MainActivity

class LessonAllViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<Int>()
    private val searchBackgroundLiveData = MutableLiveData<Int>()

    val scheduleList = ArrayList<LessonItem>()
    lateinit var background: Drawable

    val scheduleLiveData = Transformations.switchMap(searchLiveData) { query ->
        ScheduleDataGet.getSchedule(query)
    }

    val backgroundLiveData = Transformations.switchMap(searchBackgroundLiveData) { query ->
        ScheduleDataGet.getBackground(query)
    }

    fun searchSchedule(query: Int) {
        searchLiveData.value = query
    }

    fun searchBackground(query: Int) {
        searchBackgroundLiveData.value = query
    }
}