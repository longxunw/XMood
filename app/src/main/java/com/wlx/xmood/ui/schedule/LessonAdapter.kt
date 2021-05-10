package com.wlx.xmood.ui.schedule

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.schedule.edit.ScheduleEditActivity
import com.wlx.xmood.utils.TimeUtil
import java.sql.Date

class LessonAdapter(
    private val fragment: Fragment,
    private val dataList: List<LessonItem>
) :
    RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    private val TAG = "LessonAdapter"
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lessonName: TextView = view.findViewById(R.id.schedule_lesson_name)
        val lessonLocation: TextView = view.findViewById(R.id.schedule_lesson_location)
        val lessonTime: TextView = view.findViewById(R.id.schedule_lesson_time)
        val toEdit: ImageView = view.findViewById(R.id.schedule_lesson_arrow_btn)
        val line: View = view.findViewById(R.id.schedule_lesson_item_line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(context).inflate(R.layout.schedule_lesson_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        if (position == 0) {
            holder.line.isVisible = false
        }
        holder.lessonName.text = item.name
        holder.lessonLocation.text = item.location
        val startDate = Date(item.startTime)
        val endDate = Date(item.endTime)
        val timeString =
            TimeUtil.Date2Str(startDate, "HH:mm") + "-" + TimeUtil.Date2Str(endDate, "HH:mm")
        holder.lessonTime.text = timeString
        holder.toEdit.setOnClickListener {
            val intent = Intent(context, ScheduleEditActivity::class.java)
            intent.putExtra("ScheduleId", item.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}