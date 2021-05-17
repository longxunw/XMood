package com.wlx.xmood.ui.mood.edit

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.wlx.xmood.R

class CategoryItemAdapter(
    val context : Context,
    val list : ArrayList<CategoryItem>
    ) : BaseAdapter() {

    inner class ViewHolder () {
        lateinit var category: TextView
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View ?= null
        var viewHolder: ViewHolder ?= null

        if(convertView == null){
            viewHolder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.category_grid_item, null)
            viewHolder.category = view.findViewById(R.id.ctg_grid_text_item)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.category.text = list[position].category

        viewHolder.category.setOnClickListener(
            View.OnClickListener { view ->
                run {
                    viewHolder.category.setBackgroundResource(R.drawable.category_item_selected)
                }
        })

        return view!!
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }
}