package com.wlx.xmood.ui.schedule


data class LessonItem(
    val id: Int,
    val name: String,
    val location: String,
    val weekDay: Int,
    val period: Int,
    val startTime: Long,
    val endTime: Long
)
