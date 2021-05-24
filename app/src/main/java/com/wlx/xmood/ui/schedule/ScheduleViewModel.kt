package com.wlx.xmood.ui.schedule

import androidx.lifecycle.*

class ScheduleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is schedule Fragment"
    }
    val text: LiveData<String> = _text

    private val searchLiveData = MutableLiveData<String>()

    val scheduleList = ArrayList<LessonItem>()

    val scheduleLiveData = Transformations.switchMap(searchLiveData) {
        ScheduleDataGet.getSchedule()
    }

    // TODO: java中无法调用getOrNull, 要测试一下这种写法
    fun scheduleLiveDataObserver(lifecycleOwner: LifecycleOwner) = {
        this.scheduleLiveData.observe(lifecycleOwner, Observer { result ->
            val schedules = result.getOrNull()
            if (schedules != null) {
                this.scheduleList.clear()
                this.scheduleList.addAll(schedules)
            }
        })
    }

    fun searchSchedule() {
        searchLiveData.value = "query_all"
    }

}