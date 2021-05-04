package com.wlx.xmood.ui.memorandum.nav

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.MemorandumFragment
import com.wlx.xmood.utils.Utils

class MemorandumNavAdapter(val fragment: MemorandumFragment, val dataList: List<String>) :
    RecyclerView.Adapter<MemorandumNavAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.memorandum_nav_item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val root =
            LayoutInflater.from(context).inflate(R.layout.memorandum_list_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.text.text = item
        holder.text.setOnClickListener {
            Utils.makeToast(context, "change to $item")
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}