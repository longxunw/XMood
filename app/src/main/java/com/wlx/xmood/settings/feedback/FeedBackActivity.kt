package com.wlx.xmood.settings.feedback

import android.os.Bundle
import android.widget.ImageView
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R

class FeedBackActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }
}