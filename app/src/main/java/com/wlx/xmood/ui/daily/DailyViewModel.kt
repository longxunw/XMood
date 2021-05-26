package com.wlx.xmood.ui.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class DailyViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is daily Fragment"
    }
    val text: LiveData<String> = _text

    private val eventSearchLiveData = MutableLiveData<String>()
    private val daySearchLiveDate = MutableLiveData<String>()

    val eventList = ArrayList<DailyItem>()
    val dayList = ArrayList<String>()

    val eventLiveData = Transformations.switchMap(eventSearchLiveData) { query ->
        DailyDataGet.getEvent(query)
    }

    val dayLiveData = Transformations.switchMap(daySearchLiveDate) { query ->
        DailyDataGet.getEventDay(query)
    }

    fun searchEvent(query: String) {
        eventSearchLiveData.value = query
    }

    fun searchDay(query: String) {
        daySearchLiveDate.value = query
    }


}