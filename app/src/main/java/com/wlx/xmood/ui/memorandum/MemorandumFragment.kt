package com.wlx.xmood.ui.memorandum

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

class MemorandumFragment : Fragment() {

    private lateinit var memorandumViewModel: MemorandumViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        memorandumViewModel = ViewModelProvider(this).get(MemorandumViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_memorandum, container, false)
        val textView: TextView = root.findViewById(R.id.text_memorandum)

        memorandumViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val toolbar: Toolbar = root.findViewById(R.id.toolbar_memorandum)
        toolbar.inflateMenu(R.menu.memorandum_tool_bar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_memorandum ->
                    Toast.makeText(context, "add_memorandum", Toast.LENGTH_SHORT).show()
                R.id.search_memorandum ->
                    Toast.makeText(context, "search_memorandum", Toast.LENGTH_SHORT).show()
            }
            true
        }
        return root
    }


}