package com.wlx.xmood.ui.mood

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class MoodTabFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    private val hourFragment = MoodChartFragment.newInstance(MoodChartFragment.HOUR_TYPE)
    private val dayFragment = MoodChartFragment.newInstance(MoodChartFragment.DAY_TYPE)
    private val weekFragment = MoodChartFragment.newInstance(MoodChartFragment.WEEK_TYPE)
    private val monthFragment = MoodChartFragment.newInstance(MoodChartFragment.MONTH_TYPE)

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> hourFragment
            1 -> dayFragment
            2 -> weekFragment
            3 -> monthFragment
            else -> hourFragment
        }
    }

}