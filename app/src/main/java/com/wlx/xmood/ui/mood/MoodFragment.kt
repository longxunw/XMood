package com.wlx.xmood.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wlx.xmood.R

class MoodFragment : Fragment() {

    private lateinit var moodViewModel: MoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moodViewModel = ViewModelProvider(this).get(MoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mood, container, false)
        val textView: TextView = root.findViewById(R.id.text_mood)
        moodViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val addBtn: FloatingActionButton = root.findViewById(R.id.add_mood)
        addBtn.setOnClickListener {
            Toast.makeText(context, "add_mood", Toast.LENGTH_SHORT).show()
        }
        return root
    }
}