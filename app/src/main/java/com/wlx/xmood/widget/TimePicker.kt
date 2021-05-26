package com.wlx.xmood.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wlx.xmood.R
import com.wlx.xmood.utils.TimeUtil

class TimePicker(context: Context, attr: AttributeSet? = null) : AppCompatEditText(context, attr),
    OnDateSetListener {
    private var type = Type.HOURS_MINS
    private var timeLong = 0L
    private var timeStr = ""
    var current = System.currentTimeMillis()
    val pickerBuilder = TimePickerDialog.Builder()
        .setType(Type.HOURS_MINS)
        .setThemeColor(context.resources.getColor(R.color.date_picker_text_light))
        .setWheelItemTextNormalColor(context.resources.getColor(R.color.light_gray))
        .setWheelItemTextSelectorColor(context.resources.getColor(R.color.date_picker_text_light))
        .setToolBarTextColor(context.resources.getColor(R.color.date_picker_bg))
        .setWheelItemTextSize(18)
        .setCurrentMillseconds(current)
        .setCallBack(this)

    init {
        this.isFocusable = false
    }

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        timeLong = millseconds
        current = millseconds
        when (type) {
            Type.HOURS_MINS -> {
                timeStr = TimeUtil.Long2Str(millseconds, "HH:mm")
            }
            Type.YEAR_MONTH_DAY -> {
                timeStr = TimeUtil.Long2Str(millseconds, "yyyy-MM-dd")
            }
        }
        this.setText(timeStr)
    }

    fun setTime(string: String) {
        when (type) {
            Type.YEAR_MONTH_DAY -> {
                timeLong = TimeUtil.Str2Long(string, "yyyy-MM-dd")
            }
            Type.HOURS_MINS -> {
                timeLong = TimeUtil.Str2Long(string, "HH:mm")
            }
        }
        current = timeLong
        timeStr = string
        this.setText(timeStr)
    }

    fun setTime(long: Long) {
        timeLong = long
        current = long
        when (type) {
            Type.YEAR_MONTH_DAY -> {
                timeStr = TimeUtil.Long2Str(long, "yyyy-MM-dd")
            }
            Type.HOURS_MINS -> {
                timeStr = TimeUtil.Long2Str(long, "HH:mm")
            }
        }
        this.setText(timeStr)
    }

    fun getTime(): Long {
        return timeLong
    }

    fun getTimeStr(): String {
        return timeStr
    }

    fun setType(type: Type) {
        pickerBuilder.setType(type)
        this.type = type
    }
}