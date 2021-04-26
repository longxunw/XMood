package com.wlx.xmood.ui.memorandum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.utils.TimeUtil

class MemorandumFragment : Fragment() {

    private lateinit var memorandumViewModel: MemorandumViewModel

    private val noteList = ArrayList<MemorandumItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        memorandumViewModel = ViewModelProvider(this).get(MemorandumViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_memorandum, container, false)
//        val textView: TextView = root.findViewById(R.id.text_memorandum)
//
//        memorandumViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
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
        val recyclerView: RecyclerView = root.findViewById(R.id.memorandum_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MemorandumAdapter(this, noteList)
        return root
    }

    private fun init() {
        noteList.add(
            MemorandumItem(
                "TCP与UDP", "TCP是有连接，UDP是无连接",
                TimeUtil.Str2Date("2021年4月17日", "YYYY年MM月DD日"), "计网"
            )
        )
        noteList.add(
            MemorandumItem(
                "线程与进程", "进程可以拥有多个线程，进程是操作系统内存分配的基本单位",
                TimeUtil.Str2Date("2021年4月24日", "YYYY年MM月DD日"), "OS"
            )
        )
        noteList.add(
            MemorandumItem(
                "线程与协程", "协程比线程更小，可以用来执行轻量级任务",
                TimeUtil.Str2Date("2021年5月6日", "YYYY年MM月DD日"), "Android"
            )
        )
    }

}