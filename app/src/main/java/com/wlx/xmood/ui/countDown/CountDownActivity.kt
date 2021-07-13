package com.wlx.xmood.ui.countDown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.R
import com.wlx.xmood.ui.countDown.edit.CountDownEditActivity
import com.wlx.xmood.ui.schedule.LessonAdapter

class CountDownActivity : AppCompatActivity() {
    private lateinit var countDownViewModel: CountDownViewModel
    private val TAG = "CountDownActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        countDownViewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)
        val backBtn = findViewById<ImageView>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.count_down_tool_bar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.add_count_down -> {
                    val intent = Intent(this,CountDownEditActivity::class.java)
                    intent.putExtra("id",-1)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this.startActivity(intent)
                }
            }
            true
        }
        val recyclerView: RecyclerView = findViewById(R.id.count_down_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CountDownItemAdapter(countDownViewModel.countDownList)
        recyclerView.adapter = adapter

        countDownViewModel.countDownLiveData.observe(this, Observer { result ->
            val countdowns = result.getOrNull()
            if(countdowns != null){
                countDownViewModel.countDownList.clear()
                countDownViewModel.countDownList.addAll(countdowns)
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        countDownViewModel.searchCountDown("1")
    }
}