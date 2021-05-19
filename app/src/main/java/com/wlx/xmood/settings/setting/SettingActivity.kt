package com.wlx.xmood.settings.setting

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.ActivityCollector
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R

class SettingActivity : BaseActivity() {
    private val menuList = ArrayList<SetMenuItem>()

    private lateinit var adapter: SetMenuAdapter

    private val TAG = "SettingActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val back: ImageButton = findViewById(R.id.set_back_btn)
        back.setOnClickListener {
            finish()
        }
        init()
        val recyclerView: RecyclerView = findViewById(R.id.setRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SetMenuAdapter(menuList)
        recyclerView.adapter = adapter
    }

    private fun init() {
        Log.d(TAG, "isLogin: ${ActivityCollector.isLogin}")
        if (ActivityCollector.isLogin) {
            menuList.add(
                SetMenuItem(
                    R.string.set_userpsw, true, "UserInfoActivity"
                )
            )
        }

        menuList.add(
            SetMenuItem(
                R.string.set_currency, true, "CurrencyActivity"
            )
        )
        menuList.add(
            SetMenuItem(
                R.string.set_privacy, true, "PrivacyActivity"
            )
        )
        menuList.add(
            SetMenuItem(
                R.string.set_clear_cash, false, "ClearCash"
            )
        )
        menuList.add(
            SetMenuItem(
                R.string.set_about, false, "About"
            )
        )
    }
}