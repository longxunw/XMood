package com.wlx.xmood

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

object ActivityCollector {
    var onlyLocal = true
    var token = ""
    var username = ""
    lateinit var tokenSP: SharedPreferences

    val activityList = ArrayList<AppCompatActivity>()

    fun addActivity(activity: AppCompatActivity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: AppCompatActivity) {
        activityList.remove(activity)
    }

    fun finishAll() {
        for (activity in activityList) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activityList.clear()
        System.exit(0)
    }

    /**对当前的username和token进行缓存 */
    fun cacheToken(context: Context) {
        if (tokenSP == null) {
            tokenSP = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
        }
        tokenSP.edit().putString("TOKEN", token).apply()
        tokenSP.edit().putString("USERNAME", username).apply()
    }

    /**从缓存中读取username和token */
    fun getCacheToken(context: Context) {
        if (tokenSP == null) {
            tokenSP = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
        }
        token = tokenSP.getString("TOKEN", null).toString()
        username = tokenSP.getString("USERNAME", null).toString()
    }

    /**清除缓存，退出登录时使用 */
    fun clearCacheToken(context: Context) {
        if (tokenSP == null) {
            tokenSP = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
        }
        tokenSP.edit().clear().apply()
    }

}