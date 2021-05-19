package com.wlx.xmood

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlin.system.exitProcess

object ActivityCollector {
    val TAG = "ActivityCollector"
    var onlyLocal = true
    var token = ""
    var username = ""
    lateinit var tokenSP: SharedPreferences

    private val activityList = ArrayList<BaseActivity>()

    fun addActivity(activity: BaseActivity) {
        activityList.add(activity)
        Log.d(TAG, "addActivity: ${activity.toString()}")
    }

    fun removeActivity(activity: BaseActivity) {
        Log.d(TAG, "removeActivity: ${activity.toString()}")
        activityList.remove(activity)
    }

    fun finishAll() {
        Log.d(TAG, "finishAll: ${activityList.size}")
        for (activity in activityList) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activityList.clear()
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
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