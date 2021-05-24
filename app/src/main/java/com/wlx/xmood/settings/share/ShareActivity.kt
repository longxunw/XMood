package com.wlx.xmood.settings.share

import android.content.Intent
import android.os.Bundle
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R

class ShareActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        //分享文字
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "https://developer.android.com/training/sharing/")

        }, null)

        startActivity(share)
    }
}