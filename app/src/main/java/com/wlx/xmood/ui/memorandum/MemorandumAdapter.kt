package com.wlx.xmood.ui.memorandum

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.edit.MemorandumEditActivity
import com.wlx.xmood.utils.TimeUtil

class MemorandumAdapter(
    val fragment: Fragment,
    val dataList: List<MemorandumItem>,
) :
    RecyclerView.Adapter<MemorandumAdapter.ViewHolder>() {
    private val TAG = "MemorandumAdapter"
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val head: TextView = view.findViewById(R.id.memorandum_note_head)
        val bodySimple: TextView = view.findViewById(R.id.memorandum_note_body_simple)
        val updateTime: TextView = view.findViewById(R.id.memorandum_note_update_time)
        val catalog: TextView = view.findViewById(R.id.memorandum_note_catalog)
//        val deleteBtn: ImageView = view.findViewById(R.id.memorandum_note_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(context).inflate(R.layout.memorandum_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.head.text = item.head
        if (item.body.length < 40) {
            holder.bodySimple.text = item.body
        } else {
            holder.bodySimple.text = item.body.subSequence(0, 40)
        }
        holder.catalog.text = item.catalog
        val timeString = TimeUtil.Date2Str(item.updateTime, "yyyy年MM月dd日")
        holder.updateTime.text = timeString
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MemorandumEditActivity::class.java)
            intent.putExtra("id", item.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }
}