package com.wlx.xmood.ui.mood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wlx.xmood.R
import com.wlx.xmood.ui.mood.edit.MoodEditActivity

class MoodFragment : Fragment() {

    private lateinit var moodViewModel: MoodViewModel

    private val TAG = "MoodFragment"
    private lateinit var timeTab: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: MoodTabFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moodViewModel = ViewModelProvider(this).get(MoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mood, container, false)
//        val textView: TextView = root.findViewById(R.id.text_mood)
//        moodViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val addBtn: FloatingActionButton = root.findViewById(R.id.add_mood)
        addBtn.setOnClickListener {
            val intent = Intent(context, MoodEditActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        }
        adapter = MoodTabFragmentAdapter(this)
        timeTab = root.findViewById(R.id.mood_time_tab)
        viewPager = root.findViewById(R.id.mood_viewpager)
//        initTab()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager.adapter = adapter
        timeTab.setSelectedTabIndicatorHeight(0)
        TabLayoutMediator(timeTab,viewPager,true,true,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.setCustomView(context?.let {
                    MoodTabItem(it).apply {
                        text = "hour"
                        setTextColor(context.resources.getColor(R.color.black))
                    }
                })
            }).attach()
    }

//    override fun onResume() {
//        super.onResume()
//        initViewpager()
//    }

//    private  fun initViewpager(){
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                timeTab.getTabAt(position)?.select()
//            }
//        })
//        timeTab.addOnTabSelectedListener(object :
//            TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
//            override fun onTabReselected(p0: TabLayout.Tab?) {
//            }
//
//            override fun onTabUnselected(p0: TabLayout.Tab?) {
//            }
//
//            override fun onTabSelected(p0: TabLayout.Tab?) {
//                viewPager.currentItem = timeTab.selectedTabPosition
//                Log.d(TAG, "onTabSelected: ")
//            }
//        })
//
//        fragmentManager?.let {
//            adapter = MoodTabFragmentAdapter(it)
//            viewPager.adapter = adapter
////            viewPager.offscreenPageLimit = adapter.count
//        }
//    }
//
//    private fun initTab() {
//        timeTab.addTab(
//            timeTab.newTab().setCustomView(context?.let {
//                MoodTabItem(it).apply {
//                    text = "hour"
//                    setTextColor(context.resources.getColor(R.color.black))
//                }
//            })
//        )
//        timeTab.addTab(
//            timeTab.newTab().setCustomView(context?.let {
//                MoodTabItem(it).apply {
//                    text = "day"
//                    setTextColor(context.resources.getColor(R.color.light_gray))
//                }
//            })
//        )
//        timeTab.addTab(
//            timeTab.newTab().setCustomView(context?.let {
//                MoodTabItem(it).apply {
//                    text = "week"
//                    setTextColor(context.resources.getColor(R.color.light_gray))
//                }
//            })
//        )
//        timeTab.addTab(
//            timeTab.newTab()
//                .setCustomView(context?.let {
//                    MoodTabItem(it).apply {
//                        text = "month"
//                        setTextColor(context.resources.getColor(R.color.light_gray))
//                    }
//                })
//        )
//        timeTab.setSelectedTabIndicatorHeight(0)
//
//    }
}