package com.wlx.xmood.ui.countDown

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * @author  : wanglongxun
 * @date    : 2021/7/13 15:14
 */
class CountDownViewModel: ViewModel() {
    private val countDownSearchLiveData = MutableLiveData<String>()

    val countDownList = ArrayList<CountDownItem>()

    val countDownLiveData = Transformations.switchMap(countDownSearchLiveData){ query ->
        CountDownDataGet.getAll(query)
    }

    fun searchCountDown(query: String){
        countDownSearchLiveData.value = query
    }
}