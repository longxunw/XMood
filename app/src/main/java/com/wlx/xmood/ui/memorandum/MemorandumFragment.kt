package com.wlx.xmood.ui.memorandum

import android.content.Intent
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
import com.wlx.xmood.ui.memorandum.edit.MemorandumEditActivity
import com.wlx.xmood.ui.memorandum.search.MemorandumSearchFragment
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.Utils

class MemorandumFragment : Fragment() {

    private lateinit var memorandumViewModel: MemorandumViewModel

    private val noteList = ArrayList<MemorandumItem>()
    private var allnoteList = ArrayList<MemorandumItem>()
    private var navTitleList = ArrayList<String>()
    private lateinit var noteListAdapter: MemorandumAdapter
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var noteListTitle: TextView
    private lateinit var allnote: TextView
    private val TAG = "MemorandumFragment"
    private lateinit var root: View
    private var isInCatalog = false
    private var nowCatalog = "all"

    companion object {
        fun newInstance() = MemorandumFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_memorandum, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.memorandum_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        noteListAdapter = MemorandumAdapter(this, noteList)
        recyclerView.adapter = noteListAdapter
        allnote = root.findViewById(R.id.memorandum_to_all_note)
        allnote.setOnClickListener {
            changeToAll()
        }
        memorandumViewModel =
            ViewModelProvider(this).get(MemorandumViewModel::class.java)
        drawerLayout = root.findViewById(R.id.memorandum_drawer_layout)
        val toolbar: Toolbar = root.findViewById(R.id.toolbar_memorandum)
        toolbar.inflateMenu(R.menu.memorandum_tool_bar)
        toolbar.menu.findItem(R.id.add_memorandum).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.menu.findItem(R.id.search_memorandum)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_memorandum -> {
                    val intent = Intent(context, MemorandumEditActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                    Toast.makeText(context, "add_memorandum", Toast.LENGTH_SHORT).show()
                }
                R.id.search_memorandum -> {
                    Toast.makeText(context, "search_memorandum", Toast.LENGTH_SHORT).show()
                    Utils.replaceFragmentToActivity(
                        fragmentManager,
                        MemorandumSearchFragment.newInstance(),
                        R.id.nav_host_fragment
                    )
                }
            }
            true
        }
        val openDrawBtn: ImageView = root.findViewById(R.id.memorandum_drawer_btn)
        openDrawBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        noteListTitle = root.findViewById(R.id.memorandum_note_list_title)
        navView = root.findViewById(R.id.memorandum_navView)
//        savedInstanceState?.let {
//            Log.d(TAG, "onCreateView: savedInstanceState")
//            allnoteList = it.getSerializable(NOTELIST) as ArrayList<MemorandumItem>
//            navTitleList = it.getStringArrayList(NAVTITLELIST) as ArrayList<String>
//            nowCatalog = it.getString(CATALOG, "all")
//            val menu = navView.menu
//            for (i in 0 until navTitleList.size) {
//                menu.add(1, i, i, navTitleList[i]).setIcon(R.drawable.ic_point_24)
//                    .setOnMenuItemClickListener {
//                        changeToCatalog(navTitleList[i])
//                        true
//                    }
//            }
//            if (nowCatalog == "all") {
//                isInCatalog = true
//                changeToAll()
//            } else {
//                changeToCatalog(nowCatalog)
//            }
//            return root
//        }
        val menu = navView.menu
        for (i in 0 until navTitleList.size) {
            menu.add(1, i, i, navTitleList[i]).setIcon(R.drawable.ic_point_24)
                .setOnMenuItemClickListener {
                    changeToCatalog(navTitleList[i])
                    true
                }
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        Log.d(TAG, "onCreate: ")
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        Log.d(TAG, "onSaveInstanceState: ")
//        outState.putString(CATALOG, nowCatalog)
//        outState.putSerializable(NOTELIST, allnoteList)
//        outState.putSerializable(NAVTITLELIST, navTitleList)
//    }

    private fun init() {
        Log.d(TAG, "init: ")
        allnoteList.add(
            MemorandumItem(
                "TCP与UDP", "TCP是有连接，UDP是无连接",
                TimeUtil.Str2Date("2021年4月17日", "yyyy年MM月dd日"), "计网"
            )
        )
        allnoteList.add(
            MemorandumItem(
                "线程与进程", "进程可以拥有多个线程，进程是操作系统内存分配的基本单位",
                TimeUtil.Str2Date("2021年4月24日", "yyyy年MM月dd日"), "OS"
            )
        )
        allnoteList.add(
            MemorandumItem(
                "线程与协程", "协程比线程更小，可以用来执行轻量级任务",
                TimeUtil.Str2Date("2021年5月4日", "yyyy年MM月dd日"), "Android"
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
        isInCatalog = false
        nowCatalog = "all"
        allnote.visibility = View.GONE
        noteListAdapter.notifyDataSetChanged()
    }
}