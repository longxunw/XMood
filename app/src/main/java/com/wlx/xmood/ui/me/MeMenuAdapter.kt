package com.wlx.xmood.ui.me

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.settings.feedback.FeedBackActivity
import com.wlx.xmood.settings.setting.SettingActivity
import com.wlx.xmood.settings.share.ShareActivity
import com.wlx.xmood.settings.theme.ThemeSelectActivity

class MeMenuAdapter(
    private val fragment: Fragment,
    private val dataList: List<MeMenuItem>,
    private val context: Context?
) :
    RecyclerView.Adapter<MeMenuAdapter.ViewHolder>() {

    private val TAG = "MeMenuAdapter"

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img1Id: ImageView = view.findViewById(R.id.me_menu_img1)

        //        val totalItem: ConstraintLayout = view.findViewById(R.id.me_menu_item)
        val name: TextView = view.findViewById(R.id.me_menu_name)
        val img2Id: ImageView = view.findViewById(R.id.me_menu_img2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.me_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.img1Id.setImageResource(item.imgId1)
        holder.name.setText(item.nameId)
        holder.img2Id.setImageResource(item.imgId2)
        lateinit var intent: Intent
        when (item.targetActivity) {
            "ThemeSelectActivity" -> {
                intent = Intent(context, ThemeSelectActivity::class.java)
                holder.itemView.setOnClickListener {
                    fragment.startActivity(intent)
                }
            }
            "FeedBackActivity" -> {
                intent = Intent(context, FeedBackActivity::class.java)
                holder.itemView.setOnClickListener {
                    fragment.startActivity(intent)
                }
            }
            "ShareActivity" -> {
                intent = Intent(context, ShareActivity::class.java)
                holder.itemView.setOnClickListener {
                    fragment.startActivity(intent)
                }
            }
            "SettingActivity" -> {
                intent = Intent(context, SettingActivity::class.java)
                holder.itemView.setOnClickListener {
                    fragment.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}