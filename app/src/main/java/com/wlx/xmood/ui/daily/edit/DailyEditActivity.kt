package com.wlx.xmood.ui.daily.edit

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.jzxiang.pickerview.data.Type
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.daily.DailyDataGet
import com.wlx.xmood.ui.daily.DailyItem
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.Utils
import com.wlx.xmood.widget.TimePicker


class DailyEditActivity : BaseActivity() {
    private var status = 0
    private val ADD = 0
    private val UPDATE = 1
    private var id = -1
    private lateinit var event: EditText
    private lateinit var title: TextView
    private lateinit var day: TimePicker
    private lateinit var startTime: TimePicker
    private lateinit var endTime: TimePicker
    private lateinit var isAlarm: SwitchCompat
    private lateinit var alarmTime: TimePicker
    private lateinit var alarmTimeView: View
    private lateinit var dailyItem: DailyItem
    private val TAG = "DailyEditActivity"
    private var isEditted = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_edit)

        id = intent.getIntExtra("id", -1)
        title = findViewById(R.id.daily_edit_title)
        event = findViewById(R.id.daily_edit_name_text)
        day = findViewById(R.id.daily_edit_start_date_edit)
        startTime = findViewById(R.id.daily_edit_start_time_edit)
        endTime = findViewById(R.id.daily_edit_end_time_edit)
        isAlarm = findViewById(R.id.remind_switch_btn)
        alarmTime = findViewById(R.id.daily_edit_alarm_time_edit)
        alarmTimeView = findViewById(R.id.daily_remind_time)

        event.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        day.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        startTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        endTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        isAlarm.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                isEditted = true
                setAlarmView()
            }
        })

        day.setType(Type.YEAR_MONTH_DAY)
        day.setOnClickListener {
            day.pickerBuilder.setMinMillseconds(TimeUtil.Str2Long("2021-01-01 00:01","yyyy-MM-dd HH:mm")).
                setCurrentMillseconds(day.current).build().show(supportFragmentManager, "日程日期")
        }

        startTime.setType(Type.HOURS_MINS)
        startTime.setOnClickListener {
            startTime.pickerBuilder.setCurrentMillseconds(startTime.current).build()
                .show(supportFragmentManager, "日程开始时间")
        }

        endTime.setType(Type.HOURS_MINS)
        endTime.setOnClickListener {
            endTime.pickerBuilder.setCurrentMillseconds(endTime.current).build()
                .show(supportFragmentManager, "日程结束时间")
        }

        alarmTime.setType(Type.ALL)
        alarmTime.setOnClickListener {
            alarmTime.pickerBuilder.setMinMillseconds(alarmTime.current).build()
                .show(supportFragmentManager, "日程提醒时间")
        }



        if (id == -1) {
            title.text = "添加日程"
            status = ADD
            setAlarmView()
        } else {
            title.text = "编辑日程"
            dailyItem = DailyDataGet.getEventDayById(id)
            status = UPDATE
            dailyItem?.let {
                day.setTime(dailyItem.day)
                startTime.setTime(dailyItem.startTime)
                endTime.setTime(dailyItem.endTime)
                event.setText(dailyItem.event)
                isAlarm.isChecked = dailyItem.isAlarm
                if (isAlarm.isChecked) {
                    alarmTime.setTime(dailyItem.alarmTime)
                }
            }
            setAlarmView()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_daily_edit)
        toolbar.inflateMenu(R.menu.memorandum_edit_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_save -> {
                    save()
                }
                R.id.edit_delete -> {
                    showDeleteDialog()
                }
            }
            true
        }


        val backBtn: ImageButton = findViewById(R.id.change_pw_back_btn)

        backBtn.setOnClickListener {
            if (isEditted) {
                showBackDialog()
            } else {
                finish()
            }
        }
        handler.postDelayed(Runnable {
            isEditted = false
        }, 1000)

    }

    private fun showBackDialog() {
        val bottomDialog = Dialog(this, R.style.MyDialogTheme);
        val contentView = LayoutInflater.from(this).inflate(
            R.layout.exit_edit_dialog_content,
            null
        )
        bottomDialog.setContentView(contentView)
        val layoutParams: ViewGroup.MarginLayoutParams =
            contentView.layoutParams as ViewGroup.MarginLayoutParams;
        layoutParams.width =
            resources.displayMetrics.widthPixels - DensityUtil.dp2px(this, 16f)
        layoutParams.bottomMargin = DensityUtil.dp2px(this, 16f)
        contentView.layoutParams = layoutParams
        bottomDialog.window?.setGravity(Gravity.BOTTOM);
        bottomDialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog.window?.findViewById<TextView>(R.id.direct_exit)
            ?.setOnClickListener {
                bottomDialog.dismiss()
                finish()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.preserve_and_exit)
            ?.setOnClickListener {
                save()
                bottomDialog.dismiss()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.cancel)
            ?.setOnClickListener {
                bottomDialog.dismiss()
            }
        bottomDialog.show()
    }

    private fun showDeleteDialog() {
        val bottomDialog = Dialog(this, R.style.MyDialogTheme);
        val contentView = LayoutInflater.from(this).inflate(
            R.layout.delete_memorandum_dialog_content,
            null
        )
        bottomDialog.setContentView(contentView)
        val layoutParams: ViewGroup.MarginLayoutParams =
            contentView.layoutParams as ViewGroup.MarginLayoutParams;
        layoutParams.width =
            this.resources.displayMetrics.widthPixels - DensityUtil.dp2px(this, 40f)
        contentView.layoutParams = layoutParams
        bottomDialog.window?.setGravity(Gravity.CENTER)
        bottomDialog.window?.findViewById<TextView>(R.id.confirm_delete)
            ?.setOnClickListener {
                DailyDataGet.deleteDaily(id)
                Utils.makeToast(this, "已删除")
                bottomDialog.dismiss()
                finish()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.cancel)
            ?.setOnClickListener {
                bottomDialog.dismiss()
            }
        bottomDialog.show()
    }

    private fun save() {
        val newDaily = getNewDaily()
        Log.d(TAG, "save: ${newDaily.startTime} ${newDaily.endTime}")
        Log.d(TAG, "save: ${TimeUtil.Long2Str(newDaily.startTime, "yyyy-MM-dd HH:mm")} ${TimeUtil.Long2Str(newDaily.endTime, "yyyy-MM-dd HH:mm")}")
        if (newDaily.event.isEmpty()) {
            Utils.makeToast(this, "日程名不能为空")
            return
        }
        if (newDaily.day == 0L) {
            Utils.makeToast(this, "日期不能为空")
            return
        }
        if (newDaily.startTime == 0L) {
            Utils.makeToast(this, "开始时间不能为空")
            return
        }
        if (newDaily.endTime == 0L) {
            Utils.makeToast(this, "结束时间不能为空")
            return
        }
        if (newDaily.isAlarm && newDaily.alarmTime == 0L) {
            Utils.makeToast(this, "提醒时间不能为空")
            return
        }
        if (newDaily.endTime < newDaily.startTime) {
            Utils.makeToast(this, "结束时间不能小于开始时间")
            return
        }

        val result = DailyDataGet.addDaily(newDaily)
        if (result) {
            if (status == ADD && newDaily.isAlarm) {
                setAlarm(newDaily)
            } else if (status == UPDATE) {
                if (newDaily.isAlarm && !dailyItem.isAlarm) {
                    setAlarm(newDaily)
                } else if (!newDaily.isAlarm && dailyItem.isAlarm) {
                    cancelAlarm(newDaily)
                }
            }
            Utils.makeToast(this, "已保存")
            finish()
        }


    }

    private fun getNewDaily(): DailyItem {
        Log.d(TAG, "getNewDaily: ${applicationContext.toString()}")
        return DailyItem(
            id,
            day.getTime(),
            TimeUtil.LongToDayLong(startTime.getTime()),
            TimeUtil.LongToDayLong(endTime.getTime()),
            event.text.toString(),
            isAlarm.isChecked,
            if (isAlarm.isChecked) alarmTime.getTime() else 0L,
            false,
            if (status == ADD) PendingIntent.getBroadcast(
                applicationContext,
                id,
                Intent("android.xmood.daily.alarm").apply { setPackage("com.wlx.xmood") },
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            else if (dailyItem.alarmIntent == null) {
                PendingIntent.getBroadcast(
                    applicationContext,
                    id,
                    Intent("android.xmood.daily.alarm").apply { setPackage("com.wlx.xmood") },
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            } else {
                dailyItem.alarmIntent
            }
        )
    }

    private fun setAlarmView() {
        if (isAlarm.isChecked) {
            alarmTimeView.visibility = View.VISIBLE
        } else {
            alarmTimeView.visibility = View.GONE
        }
    }

    private fun setAlarm(newDaily: DailyItem) {
        Log.d(TAG, "setAlarm: 1111")
        val time = newDaily.alarmTime
        val alarm: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent("android.xmood.daily.alarm")
//        intent.setPackage("com.wlx.xmood")
//        val sender = PendingIntent.getBroadcast(
//            applicationContext,
//            newDaily.id,
//            intent,
//            PendingIntent.FLAG_CANCEL_CURRENT
//        )
        alarm.set(AlarmManager.RTC_WAKEUP, time, newDaily.alarmIntent)
//        Log.d(TAG, "setAlarm: ${dailyItem.alarmIntent.toString()}")
    }

    private fun cancelAlarm(newDaily: DailyItem) {
        val alarm: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(newDaily.alarmIntent)
    }

}