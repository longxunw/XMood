package com.wlx.xmood.ui.daily

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.daily.edit.DailyEditActivity
import com.wlx.xmood.utils.AlarmUtil
import com.wlx.xmood.utils.TimeUtil
import java.util.*

class DailyItemAdapter(
    private val fragment: DailyFragment,
    private val contentList: List<DailyItem>
) : RecyclerView.Adapter<DailyItemAdapter.ViewHolder>() {

    private val TAG = " DailyItemAdapter"
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventText: TextView = view.findViewById(R.id.daily_item_event)
        val eventTime: TextView = view.findViewById(R.id.daily_item_time)
        val confirmBtn: Button = view.findViewById(R.id.daily_finish_btn)
        val isFinish: ImageView = view.findViewById(R.id.daily_is_finish_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.daily_content_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = contentList[position]
        holder.eventText.text = item.event
        val time = TimeUtil.Date2Str(Date(item.startTime), "HH:mm") +
                "--" + TimeUtil.Date2Str(Date(item.endTime), "HH:mm")
        holder.eventTime.text = time
        if (item.isFinish) {
            holder.eventText.paintFlags = holder.eventText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.eventText.setTextColor(context.resources.getColor(R.color.pale_silver))
            holder.eventTime.paintFlags = holder.eventText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.eventTime.setTextColor(context.resources.getColor(R.color.pale_silver))
            holder.isFinish.visibility = View.VISIBLE
            holder.confirmBtn.visibility = View.GONE
        } else {
            holder.eventText.paintFlags = holder.eventText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.eventText.setTextColor(context.resources.getColor(R.color.gray))
            holder.eventTime.paintFlags = holder.eventTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.eventTime.setTextColor(context.resources.getColor(R.color.gray))
            holder.isFinish.visibility = View.GONE
            holder.confirmBtn.visibility = View.VISIBLE
        }
        holder.eventText.setOnClickListener {
            val intent = Intent(context, DailyEditActivity::class.java)
            intent.putExtra("id", item.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        holder.confirmBtn.setOnClickListener {
            DailyDataGet.dailyFinish(item.id)
            holder.eventText.paintFlags = holder.eventText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.eventText.setTextColor(context.resources.getColor(R.color.pale_silver))
            holder.eventTime.paintFlags = holder.eventText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.eventTime.setTextColor(context.resources.getColor(R.color.pale_silver))
            holder.isFinish.visibility = View.VISIBLE
            holder.confirmBtn.visibility = View.GONE
            AlarmUtil.cancelAlarm(context, item)
        }
    }

    override fun getItemCount(): Int {
        return contentList.size
    }
}