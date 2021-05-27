package com.wlx.xmood.ui.mood

import java.util.*


data class MoodChartItem(
    var id: Int,
    var date: Date,
    var rating: Int,
    var event: String,
    var category: Array<String>
)
