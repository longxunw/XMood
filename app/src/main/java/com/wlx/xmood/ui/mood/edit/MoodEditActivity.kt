package com.wlx.xmood.ui.mood.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wlx.xmood.R

class MoodEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_edit)
        val moodId = intent.getStringExtra("moodId")
        if (moodId == null) {

        }
    }
}