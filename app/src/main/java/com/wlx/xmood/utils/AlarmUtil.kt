package com.wlx.xmood.utils

import android.app.AlarmManager
import android.content.Context
import com.wlx.xmood.ui.daily.DailyItem

object AlarmUtil {

    fun setAlarm(context: Context, newDaily: DailyItem) {
        val time = newDaily.alarmTime
        val alarm: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.set(AlarmManager.RTC_WAKEUP, time, newDaily.alarmIntent)
    }

    fun cancelAlarm(context: Context, newDaily: DailyItem) {
        val alarm: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(newDaily.alarmIntent)
    }
}