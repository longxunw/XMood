package com.wlx.xmood.ui.mood.edit

import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.utils.Utils


class MoodEditActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    val list: ArrayList<CategoryItem> = ArrayList()
    val texts = arrayOf<String>("Happy", "Sad", "Angry", "Lonely")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_edit)
        val moodId = intent.getStringExtra("moodId")
        if (moodId == null) {

        }


//        val spinner: Spinner = findViewById(R.id.rating_spinner)
//
//        val arr1 = ArrayAdapter.createFromResource(
//                this,
//                R.array.rating_spinner_items,
//            android.R.layout.simple_spinner_item
//        )
//
//        arr1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        spinner.adapter = arr1
//
//        spinner.onItemSelectedListener = this

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.rating_spinner_items,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//        }

        initCategoryItem()
        val gridView: GridView = findViewById(R.id.grid_category)
        gridView.adapter = CategoryItemAdapter(this, list)


        val backBtn : ImageView = findViewById(R.id.mood_edit_back)

        backBtn.setOnClickListener {
            Utils.makeToast(this,"已取消")
            finish()
        }


    }

    private fun initCategoryItem(){
        for(i in 1..10){
            val randomNum = (0..3).random()
            list.add(CategoryItem(texts[randomNum]))
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val rating = parent?.getItemAtPosition(position).toString()
        Utils.makeToast(this, "选择Rating: $rating")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Utils.makeToast(this, "nothing select!")
    }


}