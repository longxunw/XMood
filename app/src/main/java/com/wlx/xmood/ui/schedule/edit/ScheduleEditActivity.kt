package com.wlx.xmood.ui.schedule.edit

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.utils.Utils

class ScheduleEditActivity : BaseActivity() {
    private var status = 0
    private val ADD = 0
    private val UPDATE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        val id = intent.getIntExtra("id", 0)
        val title: TextView = findViewById(R.id.schedule_edit_title)
        if (id == -1) {
            title.text = "添加课程"
            status = ADD
        } else {
            title.text = "编辑课程"
            status = UPDATE
        }

        val backBtn : ImageButton = findViewById(R.id.change_pw_back_btn)

        backBtn.setOnClickListener {
            Utils.makeToast(this,"已保存")
            finish()
        }
        
    }
}