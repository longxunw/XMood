package com.wlx.xmood.ui.schedule.edit

import android.app.Dialog
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
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.Toolbar
import com.jzxiang.pickerview.data.Type
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.schedule.LessonItem
import com.wlx.xmood.ui.schedule.ScheduleDataGet
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.Utils
import com.wlx.xmood.widget.TimePicker


class ScheduleEditActivity : BaseActivity() {
    private var status = 0
    private val ADD = 0
    private val UPDATE = 1
    private var id = -1
    private lateinit var name: EditText
    private lateinit var title: TextView
    private lateinit var location: EditText
    private lateinit var weekDay: Spinner
    private lateinit var startWeek: EditText
    private lateinit var endWeek: EditText
    private lateinit var weekType: Spinner
    private lateinit var startTime: TimePicker
    private lateinit var endTime: TimePicker
    private val TAG = "ScheduleEditActivity"
    private var isEditted = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)

        id = intent.getIntExtra("id", -1)
        title = findViewById(R.id.schedule_edit_title)
        name = findViewById(R.id.schedule_edit_name_text)
        location = findViewById(R.id.schedule_edit_location_text)
        weekDay = findViewById(R.id.schedule_edit_weekDay_spin)
        startWeek = findViewById(R.id.schedule_edit_start_week_edit)
        endWeek = findViewById(R.id.schedule_edit_end_week_edit)
        weekType = findViewById(R.id.schedule_edit_week_type_spin)
        startTime = findViewById(R.id.schedule_edit_start_time_edit)
        endTime = findViewById(R.id.schedule_edit_end_time_edit)

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        location.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })

        endWeek.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        startWeek.addTextChangedListener(object : TextWatcher {
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
        weekDay.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View?,
                arg2: Int, arg3: Long
            ) {
                isEditted = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        weekType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View?,
                arg2: Int, arg3: Long
            ) {
                isEditted = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        if (id == -1) {
            title.text = "添加课程"
            status = ADD
        } else {
            title.text = "编辑课程"
            status = UPDATE
            val lessonItem = ScheduleDataGet.getScheduleById(id)
            lessonItem?.let {
                name.setText(lessonItem.name)
                location.setText(lessonItem.location)
                weekDay.setSelection(lessonItem.weekDay, true)
                startWeek.setText(lessonItem.startWeek.toString())
                endWeek.setText(lessonItem.endWeek.toString())
                weekType.setSelection(lessonItem.weekType, true)
                startTime.setTime(lessonItem.startTime)
                endTime.setTime(lessonItem.endTime)
            }
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_schedule_edit)
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

        startTime.setType(Type.HOURS_MINS)
        startTime.setOnClickListener {
            startTime.pickerBuilder.setCurrentMillseconds(startTime.current).build()
                .show(supportFragmentManager, "课程开始时间")
        }
        endTime.setType(Type.HOURS_MINS)
        endTime.setOnClickListener {
            endTime.pickerBuilder.setCurrentMillseconds(endTime.current).build()
                .show(supportFragmentManager, "课程结束时间")
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
        }, 300)

    }

    private fun getNewLesson(): LessonItem {
        return LessonItem(
            id,
            name.text.toString(),
            location.text.toString(),
            weekDay.selectedItemPosition,
            TimeUtil.LongToDayLong(startTime.getTime()),
            TimeUtil.LongToDayLong(endTime.getTime()),
            weekType.selectedItemPosition,
            if (startWeek.text.toString().isEmpty()) 0 else startWeek.text.toString().toInt(),
            if (endWeek.text.toString().isEmpty()) 0 else endWeek.text.toString().toInt()
        )
    }

    private fun save() {
        val newLesson = getNewLesson()
        Log.d(TAG, "save: ${newLesson.startWeek},${newLesson.endWeek}")
        Log.d(
            TAG,
            "save: start:${newLesson.startTime}-${
                TimeUtil.Long2Str(
                    newLesson.startTime,
                    "yyyy-MM-dd HH:mm"
                )
            } end: ${newLesson.endTime}-${TimeUtil.Long2Str(newLesson.endTime, "yyyy-MM-dd HH:mm")}"
        )
        if (newLesson.name.isEmpty()) {
            Utils.makeToast(this, "课程名不能为空")
            return
        }
        if (newLesson.startWeek == 0) {
            Utils.makeToast(this, "起始周数不能为空")
            return
        }
        if (newLesson.endWeek == 0) {
            Utils.makeToast(this, "结束周数不能为空")
            return
        }

        if (newLesson.endWeek < newLesson.startWeek) {
            Utils.makeToast(this, "结束周数不能小于起始周数")
            return
        }
        if (newLesson.startTime == 0L) {
            Utils.makeToast(this, "开始时间不能为空")
            return
        }
        if (newLesson.endTime == 0L) {
            Utils.makeToast(this, "结束时间不能为空")
            return
        }
        if (newLesson.endTime < newLesson.startTime) {
            Utils.makeToast(this, "结束时间不能小于开始时间")
            return
        }
        val result = ScheduleDataGet.addLesson(newLesson)
        if (result) {
            Utils.makeToast(this, "已保存")
            finish()
        } else {
            Utils.makeToast(this, "该时间已有课程")
        }
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
                ScheduleDataGet.deleteLesson(id)
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

    //按下返回键时显示
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
}