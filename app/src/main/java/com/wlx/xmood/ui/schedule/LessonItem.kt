package com.wlx.xmood.ui.schedule


data class LessonItem(
    val name: String,
    val location: String,
    val weekDay: Int,
    val period: Int,
    val startTime: Long,
    val endTime: Long
)
