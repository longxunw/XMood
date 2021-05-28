package com.wlx.xmood.ui.daily

import android.app.PendingIntent

//这里的time只使用其小时单位
data class DailyItem(
    var id: Int,
    var day: Long,
    var startTime: Long,
    var endTime: Long,
    var event: String,
    var isAlarm: Boolean,
    var alarmTime: Long = 0L,
    var isFinish: Boolean,
    var alarmIntent: PendingIntent? = null
)
