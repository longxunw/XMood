package com.wlx.xmood.ui.mood.edit

import android.app.Dialog
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.daily.edit.TimePickerFragment
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.Utils


class MoodEditActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var list: ArrayList<CategoryItem> = ArrayList()
    private var texts = arrayOf<String>("Happy", "Sad", "Angry", "Lonely")
    private var hasContent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_edit)
        val moodId = intent.getStringExtra("moodId")
        if (moodId == null) {

        }

        //时间选择器触发事件
        val timeText : TextView = findViewById(R.id.mood_edit_text_time)
        timeText.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager,"timepicker")
        }

        //下拉栏
        val spinner: Spinner = findViewById(R.id.rating_spinner)

        ArrayAdapter.createFromResource(
            this,R.array.rating_spinner_items,
            android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Apply the adapter to the spinner
                spinner.adapter = adapter
        }

        //下拉栏监听事件
        spinner.onItemSelectedListener = this

        //分类标签
        initCategoryItem()
        val gridView: GridView = findViewById(R.id.grid_category)
        gridView.setSelector(R.drawable.category_item)
        gridView.adapter = CategoryItemAdapter(this, list)

        //返回按钮
        val backBtn : ImageView = findViewById(R.id.mood_edit_back)
        backBtn.setOnClickListener {
            if(hasContent){
                showBackDialog()
            }
            else{
                finish()
            }
        }

        //备注描述
        val descriptionText : EditText = findViewById(R.id.mood_description)
        descriptionText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hasContent = true //编辑过内容
            }
            override fun afterTextChanged(s: Editable?) { }
        })


        //保存按钮
        val saveBtn : Button = findViewById(R.id.mood_edit_save_btn)
        saveBtn.setOnClickListener {
            save()
            Utils.makeToast(this,"已保存")
            finish()
        }

    }

    private fun initCategoryItem(){
        for(i in 1..10){
            val randomNum = (0..3).random()
            list.add(CategoryItem(texts[randomNum],0))
        }
    }

    //保存心情节点
    private fun save(){

    }

    private fun showBackDialog() {
        val bottomDialog = Dialog(this, R.style.MyDialogTheme);
        val contentView = LayoutInflater.from(this).inflate(
            R.layout.exit_edit_dialog_content,
            null
        )
        bottomDialog.setContentView(contentView)
        val layoutParams: ViewGroup.MarginLayoutParams =
            contentView.layoutParams as ViewGroup.MarginLayoutParams;
        layoutParams.width =
            resources.displayMetrics.widthPixels - DensityUtil.dp2px(this, 16f)
        layoutParams.bottomMargin = DensityUtil.dp2px(this, 16f)
        contentView.layoutParams = layoutParams
        bottomDialog.window?.setGravity(Gravity.BOTTOM);
        bottomDialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog.window?.findViewById<TextView>(R.id.direct_exit)
            ?.setOnClickListener {
                finish()
                bottomDialog.dismiss()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.preserve_and_exit)
            ?.setOnClickListener {
                save()
                finish()
                bottomDialog.dismiss()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.select_schedule_bg_cancel)
            ?.setOnClickListener {
                bottomDialog.dismiss()
            }
        bottomDialog.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val rating = parent?.getItemAtPosition(position).toString()
        Utils.makeToast(this, "选择Rating: $rating")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Utils.makeToast(this, "nothing select!")
    }


}