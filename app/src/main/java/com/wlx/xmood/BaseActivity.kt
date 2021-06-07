package com.wlx.xmood

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.wlx.xmood.utils.Utils

open class BaseActivity : AppCompatActivity() {

    private var isExit = false

    private
    val handler = Handler(Looper.getMainLooper(), Handler.Callback {
        isExit = false
        true
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    override fun onBackPressed() {
        if (isExit) {
            ActivityCollector.finishAll()
        } else {
            isExit = true
            Utils.makeToast(applicationContext, "再次点击返回键退出程序")
            handler.sendEmptyMessageDelayed(1, 2000)
        }
    }
}