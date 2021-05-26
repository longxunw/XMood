package com.wlx.xmood.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.jzxiang.pickerview.data.Type
import com.wlx.xmood.R
import com.wlx.xmood.utils.Utils
import com.wlx.xmood.widget.TimePicker
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SemesterDateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SemesterDateFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_semester_date, container, false)
        val picker: TimePicker = root.findViewById(R.id.edit_semester_start_date_picker)
        picker.setType(Type.YEAR_MONTH_DAY)
        picker.pickerBuilder.setTitleStringId("学期开始日期")
        if (ScheduleDataGet.startDate.isNotEmpty()) {
            picker.setTime(ScheduleDataGet.startDate)
            val backBtn: ImageView = root.findViewById(R.id.back)
            backBtn.visibility = View.VISIBLE
            backBtn.setOnClickListener {
                Utils.replaceFragmentToActivity(
                    requireFragmentManager(),
                    ScheduleFragment.newInstance(),
                    R.id.nav_host_fragment
                )
            }
        }
        picker.setOnClickListener {
            picker.pickerBuilder.setCurrentMillseconds(picker.current).build()
                .show(requireFragmentManager(), "Year_month_day")

        }

        val confirmBtn: Button = root.findViewById(R.id.edit_semester_confirm)
        confirmBtn.setOnClickListener {
            val timeStr = picker.getTimeStr()
            val time = Calendar.getInstance().apply { timeInMillis = picker.getTime() }
            when {
                timeStr.isEmpty() -> {
                    com.wlx.xmood.utils.Utils.makeToast(requireContext(), "请选择学期开始日期")
                }
                time.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY -> {
                    Utils.makeToast(requireContext(), "只能选择周一")
                }
                else -> {
                    ScheduleDataGet.startDate = timeStr
                    com.wlx.xmood.utils.Utils.replaceFragmentToActivity(
                        requireFragmentManager(),
                        ScheduleFragment.newInstance(),
                        R.id.nav_host_fragment
                    )
                }
            }
        }
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SemesterDateFragment()
    }
}