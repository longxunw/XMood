package com.wlx.xmood.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.provider.Settings


object NetworkUtil {
    /**
     * 网络工具类
     */
    object NetworkUtil {
        var NETWORK_TYPE_WIFI = "WIFI"
        var NETWORK_TYPE_MOBILE = "MOBILE"
        var NETWORK_TYPE_ERROR = "ERROR"


        fun isAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            return info != null && info.isAvailable
        }

        /**
         * 判断网络连接状态
         * @param context
         */
        fun getNetType(context: Context): String {
            try {
                val connectivity = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivity != null) {
                    val info = connectivity.activeNetworkInfo
                    if (info != null && info.isConnected) {
                        if (info.state == NetworkInfo.State.CONNECTED) {
                            return if (info.type == ConnectivityManager.TYPE_WIFI) {
                                // wifi
                                NETWORK_TYPE_WIFI
                            } else {
                                // 手机网络
                                NETWORK_TYPE_MOBILE
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // 网络错误
                return NETWORK_TYPE_ERROR
            }
            // 网络错误
            return NETWORK_TYPE_ERROR
        }

        fun isWIFIActivate(context: Context): Boolean {
            return (context.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                .isWifiEnabled
        }

        /**
         * 修改Wifi状态
         * @param context
         * @param status
         * true为开启Wifi，false为关闭Wifi
         */
        fun changeWIFIStatus(context: Context, status: Boolean) {
            (context.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = status
        }

        fun startNetSettingActivity(context: Context) {
            val intent = Intent(
                Settings.ACTION_WIFI_SETTINGS
            )
            context.startActivity(intent)
        }
    }
}