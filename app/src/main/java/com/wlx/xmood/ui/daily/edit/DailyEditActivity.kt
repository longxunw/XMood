package com.wlx.xmood.ui.daily.edit

import android.os.Bundle
import android.widget.TextView
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.utils.Utils

class DailyEditActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_edit)

        val cancelBtn : TextView = findViewById(R.id.daily_edit_cancel)

        val saveBtn : TextView = findViewById(R.id.daily_edit_save)

        cancelBtn.setOnClickListener{
            Utils.makeToast(this,"取消")
            finish()
        }

        saveBtn.setOnClickListener {
            save()
            Utils.makeToast(this,"已保存")
            finish()
        }

        val setStartTimeBtn : TextView = findViewById(R.id.daily_start)

        setStartTimeBtn.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager,"timePicker")
        }

        val setEndTimeBtn : TextView = findViewById(R.id.daily_end)

        setEndTimeBtn.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
        }

    }

    private fun save(){

    }
}