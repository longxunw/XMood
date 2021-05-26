package com.wlx.xmood.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.edit.MemorandumEditActivity
import com.wlx.xmood.ui.schedule.ScheduleDataGet.startDate
import com.wlx.xmood.ui.schedule.edit.ScheduleEditActivity
import com.wlx.xmood.ui.schedule.edit.SemesterDateSetActivity
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.Utils
import java.util.*

class ScheduleFragment : Fragment() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private val TAG = "ScheduleFragment"

    companion object {
        lateinit var instance: ScheduleFragment
        fun newInstance(): ScheduleFragment {
            if (instance == null) {
                instance = ScheduleFragment()
            }
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        instance = this
        val calendar = Calendar.getInstance()
        scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        scheduleViewModel.searchSchedule(calendar.get(Calendar.DAY_OF_WEEK) - 1)
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        initSemesterDate()

        val toAllBtn: Button = root.findViewById(R.id.schedule_to_all_btn)
        toAllBtn.setOnClickListener {
            Utils.replaceFragmentToActivity(
                fragmentManager, LessonAllFragment.newInstance(), R.id.nav_host_fragment
            )
        }
        val toolbar: Toolbar = root.findViewById(R.id.toolbar_schedule)
        toolbar.inflateMenu(R.menu.schedule_tool_bar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_schedule -> {
                    Toast.makeText(context, "add_schedule", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, ScheduleEditActivity::class.java)
                    intent.putExtra("id", -1)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                }
            }
            true
        }

        val weekCount: TextView = root.findViewById(R.id.schedule_week_text)
        val weekCountStr = "第 ${TimeUtil.getWeekCount(ScheduleDataGet.startDate)} 周"
        weekCount.text = weekCountStr

        val weekDay: TextView = root.findViewById(R.id.schedule_weekday_text)
        val weekDayStr = "星期${TimeUtil.getWeekDayChinese(calendar.get(Calendar.DAY_OF_WEEK) - 1)}"
        weekDay.text = weekDayStr

        val day: TextView = root.findViewById(R.id.schedule_day_text)
        val dayStr = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        day.text = dayStr

        val month: TextView = root.findViewById(R.id.schedule_month_text)
        val monthStr = String.format("%02d月", calendar.get(Calendar.MONTH) + 1)
        month.text = monthStr
        Log.d(TAG, "onCreateView: $monthStr")

        val year: TextView = root.findViewById(R.id.schedule_year_text)
        year.text = calendar.get(Calendar.YEAR).toString()

        val recyclerView: RecyclerView = root.findViewById(R.id.schedule_today_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = LessonAdapter(this, scheduleViewModel.scheduleList)
        recyclerView.adapter = adapter

        val noLessonText: TextView = root.findViewById(R.id.schedule_no_lesson)

        scheduleViewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer { result ->
            val events = result.getOrNull()
            if (events != null) {
                noLessonText.visibility = View.GONE
                scheduleViewModel.scheduleList.clear()
                scheduleViewModel.scheduleList.addAll(events)
                recyclerView.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            } else {
                noLessonText.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        })
        return root
    }

    override fun onResume() {
        super.onResume()
        scheduleViewModel.searchSchedule(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1)
    }

    private fun initSemesterDate() {
        if (startDate.isEmpty()) {
            val intent = Intent(context, SemesterDateSetActivity::class.java)
            intent.putExtra("data", "refresh")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }
}