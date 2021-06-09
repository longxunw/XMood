package com.wlx.xmood.ui.memorandum.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wlx.xmood.ui.memorandum.MemoDataGet
import com.wlx.xmood.ui.memorandum.MemorandumItem

class MemorandumSearchViewModel : ViewModel() {
    val noteList = ArrayList<MemorandumItem>()
    private val searchNoteLiveData = MutableLiveData<String>()
    val noteLiveData = Transformations.switchMap(searchNoteLiveData) { query ->
        MemoDataGet.searchNote(query)
    }

    fun searchNote(query: String) {
        searchNoteLiveData.value = query
    }
}