package com.wlx.xmood.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wlx.xmood.R

class ScheduleFragment : Fragment() {

    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        val textView: TextView = root.findViewById(R.id.text_schedule)
        scheduleViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val toolbar: Toolbar = root.findViewById(R.id.toolbar_schedule)
        toolbar.inflateMenu(R.menu.schedule_tool_bar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_schedule ->
                    Toast.makeText(context, "add_schedule", Toast.LENGTH_SHORT).show()
            }
            true
        }
        return root
    }
}