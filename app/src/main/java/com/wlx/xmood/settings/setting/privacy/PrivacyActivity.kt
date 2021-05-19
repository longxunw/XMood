package com.wlx.xmood.settings.setting.privacy

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import com.wlx.xmood.ActivityCollector
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.utils.Utils

class PrivacyActivity : BaseActivity() {

    private val viewModel: PrivacyViewModel by viewModels()
    private val TAG = "PrivacyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        val switch: SwitchCompat = findViewById(R.id.privacy_switch_btn)
        switch.setOnCheckedChangeListener { _, isChecked ->
            ActivityCollector.onlyLocal = isChecked
            Utils.makeToast(this, "OnlyLocal: " + ActivityCollector.onlyLocal)
        }
        val back: ImageButton = findViewById(R.id.privacy_back_btn)
        back.setOnClickListener {
            finish()
        }
    }
}