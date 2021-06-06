package com.wlx.xmood.ui.mood

import kotlin.collections.ArrayList


data class MoodChartItem(
    var id: Int,
    var date: Long,
    var rating: Int,
    var event: String,
    var category: ArrayList<String>
)
