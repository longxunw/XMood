package com.wlx.xmood.settings.setting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.settings.setting.currency.CurrencyActivity
import com.wlx.xmood.settings.setting.privacy.PrivacyActivity
import com.wlx.xmood.settings.setting.userinfo.UserInfoActivity
import com.wlx.xmood.utils.Utils

class SetMenuAdapter(
    private val dataList: List<SetMenuItem>,
) :
    RecyclerView.Adapter<SetMenuAdapter.ViewHolder>() {
    private lateinit var context: Context
    private val TAG = "SetMenuAdapter"

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgId: ImageView = view.findViewById(R.id.set_menu_img)
        val name: TextView = view.findViewById(R.id.set_menu_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(context).inflate(R.layout.set_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        if (item.isImaged) {
            holder.imgId.setImageResource(R.drawable.ic_me_arrow_24)
        }
        holder.name.setText(item.nameId)
        if (position == 0) {
            val layoutParams = ConstraintLayout.LayoutParams(holder.itemView.layoutParams)
            layoutParams.setMargins(0, 0, 0, 0)
            holder.itemView.layoutParams = layoutParams
        }
        when (item.target) {
            "UserInfoActivity" -> {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, UserInfoActivity::class.java)
                    context.startActivity(intent)
                }
            }
            "CurrencyActivity" -> {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, CurrencyActivity::class.java)
                    context.startActivity(intent)
                }
            }
            "PrivacyActivity" -> {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, PrivacyActivity::class.java)
                    context.startActivity(intent)
                }
            }
            "ClearCash" -> {
                holder.itemView.setOnClickListener {
                    Utils.makeToast(context, "ClearCash!")
                }
            }
            "About" -> {
                holder.itemView.setOnClickListener {
                    Utils.makeToast(context, "This is XMood!")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}