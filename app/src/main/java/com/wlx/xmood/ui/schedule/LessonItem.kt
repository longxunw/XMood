package com.wlx.xmood.ui.schedule


data class LessonItem(
    val id: Int,
    val name: String,
    val location: String,
    val weekDay: Int,
    val startTime: Long,
    val endTime: Long,
    val weekType: Int // 0没有单双周 1单周 2双周
)
