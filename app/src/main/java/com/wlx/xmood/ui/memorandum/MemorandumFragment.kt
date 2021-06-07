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
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.edit.MemorandumEditActivity
import com.wlx.xmood.ui.memorandum.search.MemorandumSearchFragment
import com.wlx.xmood.utils.Utils

class MemorandumFragment : Fragment() {

    private lateinit var memorandumViewModel: MemorandumViewModel

    private lateinit var noteListAdapter: MemorandumAdapter
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var noteListTitle: TextView
    private lateinit var allnote: TextView
    private val TAG = "MemorandumFragment"
    private lateinit var root: View

    companion object {
        fun newInstance() = MemorandumFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        memorandumViewModel =
            ViewModelProvider(this).get(MemorandumViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_memorandum, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.memorandum_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        memorandumViewModel.searchNote("__all")
//        memorandumViewModel.searchNavTitle("")
        noteListAdapter = MemorandumAdapter(this, memorandumViewModel.noteList)
        recyclerView.adapter = noteListAdapter
        allnote = root.findViewById(R.id.memorandum_to_all_note)
        allnote.setOnClickListener {
            changeToCatalog("__all")
        }
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
                }
                R.id.search_memorandum -> {
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

        noteListTitle = root.findViewById(R.id.memorandum_note_list_title) //标题 《所有笔记》
        navView = root.findViewById(R.id.memorandum_navView) //侧边栏
        val menu = navView.menu
//        for (i in 0 until memorandumViewModel.navTitleList.size) {
//            menu.add(1, i, i, memorandumViewModel.navTitleList[i]).setIcon(R.drawable.ic_point_24)
//                .setOnMenuItemClickListener {
//                    changeToCatalog(memorandumViewModel.navTitleList[i])
//                    true
//                }
//        }

        memorandumViewModel.noteLiveData.observe(viewLifecycleOwner, Observer { result ->
            val notes = result.getOrNull()
            if (notes != null) {
                memorandumViewModel.noteList.clear()
                memorandumViewModel.noteList.addAll(notes)
                noteListAdapter.notifyDataSetChanged()
            } else {
                Log.d(TAG, "onCreateView: noteResult null!")
            }
        })


        memorandumViewModel.navTitleLiveData.observe(viewLifecycleOwner, Observer { result ->
            val navTitles = result.getOrNull()
            if (navTitles != null) {
                memorandumViewModel.navTitleList.clear()
                memorandumViewModel.navTitleList.addAll(navTitles)
                menu.clear()
                for (i in 0 until memorandumViewModel.navTitleList.size) {
                    menu.add(1, i, i, memorandumViewModel.navTitleList[i])
                        .setIcon(R.drawable.ic_point_24)
                        .setOnMenuItemClickListener {
                            changeToCatalog(memorandumViewModel.navTitleList[i])
                            true
                        }
                }
            }
        })

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onResume() {
        super.onResume()
        memorandumViewModel.searchNote(MemoDataGet.mcatalog)
        memorandumViewModel.searchNavTitle("")
    }

    private fun changeToCatalog(catalog: String) {

        memorandumViewModel.searchNote(catalog)
        if (catalog == "__all") {
            noteListTitle.text = "所有笔记"
            allnote.visibility = View.GONE
        } else {
            noteListTitle.text = catalog
            allnote.visibility = View.VISIBLE
            drawerLayout.closeDrawer(GravityCompat.START)
        }

    }

}