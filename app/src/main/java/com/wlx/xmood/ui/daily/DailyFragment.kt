package com.wlx.xmood.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wlx.xmood.R

class DailyFragment : Fragment() {

    companion object {
        fun newInstance() = DailyFragment()
    }

    private lateinit var viewModel: DailyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DailyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_daily, container, false)
        val textView: TextView = root.findViewById(R.id.text_daily)
        viewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        val toolbar: Toolbar = root.findViewById(R.id.toolbar_daily)
        toolbar.inflateMenu(R.menu.daily_tool_bar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_daily ->
                    Toast.makeText(context, "add_daily", Toast.LENGTH_SHORT).show()
            }
            true
        }
        return root
    }

}