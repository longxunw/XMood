package com.wlx.xmood.ui.memorandum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MemorandumViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is memorandum Fragment"
    }
    val text: LiveData<String> = _text
}