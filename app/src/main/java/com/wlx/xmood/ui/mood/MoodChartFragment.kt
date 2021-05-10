package com.wlx.xmood.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wlx.xmood.R

class MoodChartFragment(private val timeType: Int) : Fragment() {

    companion object {
        fun newInstance(timeType: Int) = MoodChartFragment(timeType)
        val HOUR_TYPE = 0
        val DAY_TYPE = 1
        val WEEK_TYPE = 2
        val MONTH_TYPE = 3
    }

    private lateinit var viewModel: MoodChartViewModel
    private lateinit var root: View
    private lateinit var text: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.mood_chart_fragment, container, false)
        text = root.findViewById(R.id.mood_chart_test_text)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoodChartViewModel::class.java)
        when (timeType) {
            0 -> text.text = "this is Hour Chart"
            1 -> text.text = "this is Day Chart"
            2 -> text.text = "this is Week Chart"
            3 -> text.text = "this is Month Chart"
            else -> text.text = "this is Hour Chart"
        }
    }

}