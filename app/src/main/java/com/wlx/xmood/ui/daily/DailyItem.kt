package com.wlx.xmood.ui.daily

import android.app.PendingIntent
import java.util.*

//这里的time只使用其小时单位
data class DailyItem(
    var id: Int,
    var day: Date,
    var startTime: Long,
    var endTime: Long,
    var event: String,
    var isAlarm: Int,
    var alarmTime: Long = 0L,
    var isFinish: Int,
    var alarmIntent: PendingIntent? = null
)
