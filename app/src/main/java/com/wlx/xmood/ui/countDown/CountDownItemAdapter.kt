package com.wlx.xmood.ui.countDown

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.countDown.edit.CountDownEditActivity
import com.wlx.xmood.utils.TimeUtil
import java.util.*

/**
 * @author  : wanglongxun
 * @date    : 2021/7/13 10:53
 */
class CountDownItemAdapter(
    private val contentList: List<CountDownItem>
): RecyclerView.Adapter<CountDownItemAdapter.ViewHolder>() {
    private val TAG = "CountDownItemAdapter"
    private lateinit var context: Context
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val content: TextView = view.findViewById(R.id.content)
        val startDay: TextView = view.findViewById(R.id.startDay)
        val countDown: TextView = view.findViewById(R.id.count_down)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.count_down_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = contentList[position]
        holder.content.text = item.content
        holder.startDay.text = TimeUtil.Long2Str(item.startDay,"yyyy-MM-dd")
        val str = TimeUtil.getGapDayCount(
            TimeUtil.Str2Date("2019-12-03","yyyy-MM-dd"),
            Date(System.currentTimeMillis())
        ).toString() + "天"
        holder.countDown.text = str
        holder.itemView.setOnClickListener {
            val intent = Intent(context,CountDownEditActivity::class.java)
            intent.putExtra("id",item.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contentList.size
    }
}