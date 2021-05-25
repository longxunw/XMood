package com.wlx.xmood.ui.memorandum

import java.util.*

data class MemorandumItem(
    var id: Int,
    var head: String,
    var body: String,
    var updateTime: Date,
    var catalog: String
)
