package com.wlx.xmood.settings.setting.userinfo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.wlx.xmood.ActivityCollector
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R

class UserInfoActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val backBtn: ImageView = findViewById(R.id.user_info_back_btn)
        backBtn.setOnClickListener {
            finish()
        }
        val pwChange: ConstraintLayout = findViewById(R.id.user_info_pw_layout)
        pwChange.setOnClickListener {
            val intent = Intent(this, ChangePWActivity::class.java)
            intent.putExtra("username", ActivityCollector.username)
            startActivity(intent)
        }
    }
}