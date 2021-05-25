package com.wlx.xmood.ui.memorandum

import java.util.*

data class MemorandumItem(
    val id: Int,
    val head: String,
    val body: String,
    val updateTime: Date,
    val catalog: String
)
