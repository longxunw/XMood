package com.wlx.xmood.ui.memorandum

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.nav.MemorandumNavAdapter
import com.wlx.xmood.utils.TimeUtil

class MemorandumFragment : Fragment() {

    private lateinit var memorandumViewModel: MemorandumViewModel

    private val noteList = ArrayList<MemorandumItem>()
    private val allnoteList = ArrayList<MemorandumItem>()
    private val navTitleList = ArrayList<String>()
    private lateinit var noteListAdapter: MemorandumAdapter
    private lateinit var navListAdapter: MemorandumNavAdapter
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var noteListTitle: TextView
    private lateinit var allnote: TextView
    private val TAG = "MemorandumFragment"
    private lateinit var root: View
    private var isInCatalog = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        setHasOptionsMenu(true)
        memorandumViewModel =
            ViewModelProvider(this).get(MemorandumViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_memorandum, container, false)
        drawerLayout = root.findViewById(R.id.memorandum_drawer_layout)
        val toolbar: Toolbar = root.findViewById(R.id.toolbar_memorandum)
        toolbar.inflateMenu(R.menu.memorandum_tool_bar)
        toolbar.menu.findItem(R.id.add_memorandum).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.menu.findItem(R.id.search_memorandum)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_memorandum ->
                    Toast.makeText(context, "add_memorandum", Toast.LENGTH_SHORT).show()
                R.id.search_memorandum ->
                    Toast.makeText(context, "search_memorandum", Toast.LENGTH_SHORT).show()
            }
            true
        }
        val openDrawBtn: ImageView = root.findViewById(R.id.memorandum_drawer_btn)
        openDrawBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navView = root.findViewById(R.id.memorandum_navView)
        val menu = navView.menu
        for (i in 0 until navTitleList.size) {
            menu.add(1, i, i, navTitleList[i]).setIcon(R.drawable.ic_point_24)
                .setOnMenuItemClickListener {
                    changeToCatalog(navTitleList[i])
                    true
                }
        }
        val recyclerView: RecyclerView = root.findViewById(R.id.memorandum_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        noteListAdapter = MemorandumAdapter(this, noteList)
        recyclerView.adapter = noteListAdapter
        allnote = root.findViewById(R.id.memorandum_to_all_note)
        allnote.setOnClickListener {
            changeToAll()
        }
        noteListTitle = root.findViewById(R.id.memorandum_note_list_title)

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    private fun init() {
        allnoteList.add(
            MemorandumItem(
                "TCP与UDP", "TCP是有连接，UDP是无连接",
                TimeUtil.Str2Date("2021年4月17日", "YYYY年MM月DD日"), "计网"
            )
        )
        allnoteList.add(
            MemorandumItem(
                "线程与进程", "进程可以拥有多个线程，进程是操作系统内存分配的基本单位",
                TimeUtil.Str2Date("2021年4月24日", "YYYY年MM月DD日"), "OS"
            )
        )
        allnoteList.add(
            MemorandumItem(
                "线程与协程", "协程比线程更小，可以用来执行轻量级任务",
                TimeUtil.Str2Date("2021年5月6日", "YYYY年MM月DD日"), "Android"
            )
        )
        for (note in allnoteList) {
            noteList.add(note)
        }
        navTitleList.add("计网")
        navTitleList.add("OS")
        navTitleList.add("Android")
    }

    private fun changeToCatalog(catalog: String) {
        isInCatalog = true
        noteList.clear()
        for (note in allnoteList) {
            if (note.catalog == catalog) {
                noteList.add(note)
            }
        }
        noteListTitle.text = catalog
        allnote.visibility = View.VISIBLE
        noteListAdapter.notifyDataSetChanged()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun changeToAll() {
        if (!isInCatalog) {
            return
        }
        noteList.clear()
        for (note in allnoteList) {
            noteList.add(note)
        }
        noteListTitle.setText(R.string.memorandum_all_note)
        allnote.visibility = View.GONE
        noteListAdapter.notifyDataSetChanged()
    }
}