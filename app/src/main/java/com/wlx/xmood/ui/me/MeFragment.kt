package com.wlx.xmood.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.ActivityCollector
import com.wlx.xmood.R

class MeFragment : Fragment() {
    private var menuList = arrayListOf<MeMenuItem>()

    private lateinit var adapter: MeMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        val root = inflater.inflate(R.layout.fragment_me, container, false)
//        val textView: TextView = root.findViewById(R.id.text_me)
//        meViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.me_menu_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = MeMenuAdapter(this, menuList, context)
        recyclerView.adapter = adapter
        val exitBtn: ConstraintLayout = root.findViewById(R.id.me_menu_exit)
        exitBtn.setOnClickListener {
            ActivityCollector.finishAll()
        }
        return root
    }

    private fun init() {
        menuList.add(
            MeMenuItem(
                R.drawable.ic_me_theme_24,
                R.string.me_menu_theme, R.drawable.ic_me_arrow_24, "ThemeSelectActivity"
            )
        )
        menuList.add(
            MeMenuItem(
                R.drawable.ic_feedback_24, R.string.me_menu_feedback,
                R.drawable.ic_me_arrow_24, "FeedBackActivity"
            )
        )
        menuList.add(
            MeMenuItem(
                R.drawable.ic_share_24, R.string.me_menu_share,
                R.drawable.ic_me_arrow_24, "ShareActivity"
            )
        )
        menuList.add(
            MeMenuItem(
                R.drawable.ic_settings_24, R.string.me_menu_setting,
                R.drawable.ic_me_arrow_24, "SettingActivity"
            )
        )
    }
}