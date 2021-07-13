package com.wlx.xmood

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.wlx.xmood.dao.MyDatabaseHelper
import com.wlx.xmood.ui.countDown.CountDownDataGet
import com.wlx.xmood.ui.daily.DailyDataGet
import com.wlx.xmood.ui.me.MeDataGet
import com.wlx.xmood.ui.memorandum.MemoDataGet
import com.wlx.xmood.ui.mood.MoodDataGet
import com.wlx.xmood.ui.schedule.ScheduleDataGet

class MainActivity : BaseActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var selectedFragment: MenuItem

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val databaseHelper = MyDatabaseHelper(applicationContext, "Xmood.db", 1)
        MemoDataGet.dbHelper = databaseHelper
        ScheduleDataGet.dbHelper = databaseHelper
        MoodDataGet.dbHelper = databaseHelper
        DailyDataGet.dbHelper = databaseHelper
        MeDataGet.dbHelper = databaseHelper
        CountDownDataGet.dbHelper = databaseHelper
//        supportActionBar?.hide()
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_mood, R.id.navigation_memorandum,
//                R.id.navigation_schedule, R.id.navigation_me
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        setTheme(R.style.Theme_XMood1)
        Log.d(TAG, "onCreate: ${intent.getStringExtra("target")}")
        when (intent.getStringExtra("target")) {
            "daily" -> {
                selectedFragment = navView.menu.getItem(1)
                navView.selectedItemId = selectedFragment.itemId
            }
            else -> {
//                selectedFragment = navView.menu.getItem(0)
            }
        }
//        navView.setOnNavigationItemReselectedListener {
//            selectedFragment = it
//        }
    }

    override fun onResume() {
        super.onResume()
//        navView.selectedItemId = selectedFragment.itemId
    }
}