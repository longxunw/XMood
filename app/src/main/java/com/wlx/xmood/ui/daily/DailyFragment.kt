package com.wlx.xmood.ui.daily

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.wlx.xmood.R
import com.wlx.xmood.ui.daily.edit.DailyEditActivity
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.Utils
import java.util.*
import kotlin.collections.HashMap


class DailyFragment : Fragment(), CalendarView.OnCalendarSelectListener {

    companion object {
        fun newInstance() = DailyFragment()
    }

    private lateinit var viewModel: DailyViewModel
    private val schemeDate = HashMap<String, Calendar>()
    private lateinit var dayText: TextView
    private lateinit var monthText: TextView
    private lateinit var yearText: TextView
    private lateinit var weekdayText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DailyItemAdapter
    private lateinit var noEventText: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        viewModel = ViewModelProvider(this).get(DailyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_daily, container, false)
        noEventText = root.findViewById(R.id.daily_no_event)
        viewModel.searchEvent(TimeUtil.Date2Str(Date(), "yyyy-MM-dd"))
        val toolbar: Toolbar = root.findViewById(R.id.toolbar_daily)
        toolbar.inflateMenu(R.menu.daily_tool_bar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_daily -> {
                    val intent = Intent(context, DailyEditActivity::class.java) //添加日程activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                }
            }
            true
        }
        val calendar: CalendarView = root.findViewById(R.id.daily_calendar_view)
        calendar.setSchemeDate(schemeDate)
        calendar.setOnCalendarSelectListener(this)
        dayText = root.findViewById(R.id.daily_day_text)
        monthText = root.findViewById(R.id.daily_month_text)
        yearText = root.findViewById(R.id.daily_year_text)
        weekdayText = root.findViewById(R.id.daily_weekday)
        recyclerView = root.findViewById(R.id.daily_content_list_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = DailyItemAdapter(this, viewModel.eventList)
        recyclerView.adapter = adapter
        val weekDayBottomLine: View = root.findViewById(R.id.daily_weekday_bottom_line)
        viewModel.eventLiveData.observe(viewLifecycleOwner, Observer { result ->
            val events = result.getOrNull()
            if (events != null) {
                noEventText.visibility = View.GONE
                weekDayBottomLine.visibility = View.GONE
                viewModel.eventList.clear()
                viewModel.eventList.addAll(events)
                recyclerView.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            } else {
                weekDayBottomLine.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                noEventText.visibility = View.VISIBLE
            }
        })
        return root
    }

    private fun init() {
        schemeDate[getSchemeCalendar(2021, 5, 20, R.color.purple_500, "事").toString()] =
            getSchemeCalendar(2021, 5, 20, R.color.purple_500, "事")
        schemeDate[getSchemeCalendar(2021, 5, 10, R.color.purple_500, "事").toString()] =
            getSchemeCalendar(2021, 5, 10, R.color.purple_500, "事")
        schemeDate[getSchemeCalendar(2021, 5, 2, R.color.purple_500, "事").toString()] =
            getSchemeCalendar(2021, 5, 2, R.color.purple_500, "事")
        schemeDate[getSchemeCalendar(2021, 5, 25, R.color.purple_500, "日").toString()] =
            getSchemeCalendar(2021, 5, 25, R.color.purple_500, "日")
        schemeDate[getSchemeCalendar(2021, 5, 17, R.color.purple_500, "日").toString()] =
            getSchemeCalendar(2021, 5, 17, R.color.purple_500, "日")
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String
    ): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        return calendar
    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar?.let {
            val day = String.format("%02d", it.day)
            dayText.text = day
            val month = String.format("%02d", it.month) + "月"
            monthText.text = month
            yearText.text = it.year.toString()
            val weekday = "星期${TimeUtil.getWeekDayChinese(it.week)}"
            weekdayText.text = weekday
            viewModel.searchEvent("${it.year}-${String.format("%02d", it.month)}-$day")
        }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {
        context?.let { Utils.makeToast(it, "超出日期范围") }
    }
}