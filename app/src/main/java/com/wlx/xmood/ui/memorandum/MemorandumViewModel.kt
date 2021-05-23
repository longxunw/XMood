package com.wlx.xmood.ui.memorandum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MemorandumViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is memorandum Fragment"
    }
    val text: LiveData<String> = _text

    private val searchNoteLiveData = MutableLiveData<String>()
    private val searchAllnoteLiveData = MutableLiveData<String>()
    private val searchNavTitleLiveData = MutableLiveData<String>()

    val noteList = ArrayList<MemorandumItem>()
    val allnoteList = ArrayList<MemorandumItem>()
    val navTitleList = ArrayList<String>()

    val noteLiveData = Transformations.switchMap(searchNoteLiveData) {
        MemoDataGet.getNote()
    }

    val allnoteLiveData = Transformations.switchMap(searchAllnoteLiveData) {
        MemoDataGet.getAllnote()
    }


    val navTitleLiveData = Transformations.switchMap(searchNavTitleLiveData) {
        MemoDataGet.getNavTitle()
    }

    fun searchNote() {
        searchNoteLiveData.value = "query_all"
    }

    fun searchAllnote() {
        searchAllnoteLiveData.value = "query_all"
    }


    fun searchNavTitle() {
        searchNavTitleLiveData.value = "query_all"
    }

}