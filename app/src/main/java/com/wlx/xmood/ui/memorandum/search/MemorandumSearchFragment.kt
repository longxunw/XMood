package com.wlx.xmood.ui.memorandum.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.MemorandumAdapter
import com.wlx.xmood.ui.memorandum.MemorandumFragment
import com.wlx.xmood.ui.memorandum.MemorandumItem
import com.wlx.xmood.utils.Utils

class MemorandumSearchFragment : Fragment() {

    companion object {
        fun newInstance() = MemorandumSearchFragment()
    }

    private lateinit var viewModel: MemorandumSearchViewModel
    private val searchList = ArrayList<MemorandumItem>()
    private lateinit var adapter: MemorandumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.memorandum_search_fragment, container, false)
        val backBtn: ImageView = root.findViewById(R.id.memorandum_search_back_btn)
        backBtn.setOnClickListener {
            Utils.replaceFragmentToActivity(
                fragmentManager,
                MemorandumFragment.newInstance(),
                R.id.nav_host_fragment
            )
        }
        val recyclerView: RecyclerView = root.findViewById(R.id.memorandum_search_list)
        adapter = MemorandumAdapter(this, searchList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        val searchEdit: EditText = root.findViewById(R.id.memorandum_search_text)
        searchEdit.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                }
            }
            true
        }
        val searchBtn: TextView = root.findViewById(R.id.search_btn)
        searchBtn.setOnClickListener {
            search()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MemorandumSearchViewModel::class.java)
    }

    private fun search() {
        Utils.makeToast(requireContext(), "search")
        adapter.notifyDataSetChanged()
    }
}