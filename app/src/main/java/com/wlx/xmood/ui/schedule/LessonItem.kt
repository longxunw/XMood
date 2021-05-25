package com.wlx.xmood.ui.schedule


data class LessonItem(
    var id: Int,
    var name: String,
    var location: String,
    var weekDay: Int,
    var startTime: Long,
    var endTime: Long,
    var weekType: Int, // 0没有单双周 1单周 2双周
    var startWeek: Int,
    var endWeek: Int
)
