package com.wlx.xmood.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.wlx.xmood.ui.daily.DailyItem

object AlarmUtil {

    fun setAlarm(context: Context, newDaily: DailyItem) {
        val time = newDaily.alarmTime
        val alarm: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            newDaily.id,
            Intent("android.xmood.daily.alarm").apply {
                setPackage("com.wlx.xmood")
                putExtra("event", newDaily.event)
                putExtra("id", newDaily.id)
            },
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarm.set(AlarmManager.RTC_WAKEUP, time, alarmIntent)
    }

    fun cancelAlarm(context: Context, newDaily: DailyItem) {
        val alarm: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            newDaily.id,
            Intent("android.xmood.daily.alarm").apply {
                setPackage("com.wlx.xmood")
                putExtra("event", newDaily.event)
                putExtra("id", newDaily.id)
            },
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarm.cancel(alarmIntent)
    }
}