package com.wlx.xmood.ui.memorandum.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.MemorandumAdapter
import com.wlx.xmood.ui.memorandum.MemorandumFragment
import com.wlx.xmood.utils.Utils

class MemorandumSearchFragment : Fragment() {

    companion object {
        fun newInstance() = MemorandumSearchFragment()
    }

    private lateinit var viewModel: MemorandumSearchViewModel
    private lateinit var adapter: MemorandumAdapter
    private lateinit var searchEdit: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MemorandumSearchViewModel::class.java)
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
        adapter = MemorandumAdapter(this, viewModel.noteList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        searchEdit = root.findViewById(R.id.memorandum_search_text)
//        searchEdit.setOnEditorActionListener { v, actionId, event ->
//            when (actionId) {
//                EditorInfo.IME_ACTION_SEARCH -> {
//                    search()
//                }
//            }
//            true
//        }
        viewModel.noteLiveData.observe(viewLifecycleOwner, Observer { result ->
            val notes = result.getOrNull()
            if (notes != null) {
                viewModel.noteList.clear()
                viewModel.noteList.addAll(notes)
                adapter.notifyDataSetChanged()
            }
        })

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
        viewModel.searchNote(searchEdit.text.toString())
    }
}