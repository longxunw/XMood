package com.wlx.xmood.ui.mood.edit

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jzxiang.pickerview.data.Type
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.mood.MoodChartItem
import com.wlx.xmood.ui.mood.MoodDataGet
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.Utils
import com.wlx.xmood.widget.TimePicker
import java.util.*
import kotlin.collections.ArrayList


class MoodEditActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var list: ArrayList<CategoryItem> = ArrayList()
    private var texts = arrayOf<String>("Joyful", "Sorrowful", "Angry", "Lonely","Boring","Frightened"
        ,"Disgusted","Missing","Wishing","Loving")
    private var isEditted = false
    private lateinit var time: TimePicker
    private lateinit var descriptionText : EditText
    private var rating: Int = 0
    private var id: Int = -1
    private var category =  ArrayList<String>()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_edit)
        val moodId = intent.getStringExtra("moodId")
        if (moodId == null) {

        }

        id = intent.getIntExtra("id",-1)

        //时间选择器
        time = findViewById(R.id.mood_node_timepicker)
        time.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true

//                val c: Calendar = Calendar.getInstance()
//                c.timeInMillis = time.getTime()
//                val month = c.get(Calendar.MONTH)
//                Log.d("timepicker",TimeUtil.Long2Str(time.getTime(),"yyyy-MM-dd") )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        time.setType(Type.ALL)
        time.setOnClickListener {
            time.pickerBuilder.setMinMillseconds(time.current).build()
                .show(supportFragmentManager,"时间")
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
            if(isEditted){
                showBackDialog()
            }
            else{
                finish()
            }
        }

        //备注描述
        descriptionText = findViewById(R.id.mood_description)
        descriptionText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true //编辑过内容
            }
            override fun afterTextChanged(s: Editable?) { }
        })


        //保存按钮
        val saveBtn : Button = findViewById(R.id.mood_edit_save_btn)
        saveBtn.setOnClickListener {
            save()
//            Utils.makeToast(this,"已保存")
//            finish()
        }

        handler.postDelayed(Runnable {
            isEditted = false
        }, 300)
    }

    private fun initCategoryItem(){
        for(i in 0 until 10){
//            val randomNum = (0..3).random()
            list.add(CategoryItem(texts[i],0))
        }
    }

    private fun getNewNode(): MoodChartItem{
        //遍历分类标签列表
//        Log.d("id:" , id.toString() )
//        Log.d("time:" , time.getTime().toString() )
//        Log.d("rating:" , rating.toString())
//        Log.d("event:" , descriptionText.text.toString() )

        for (node in list){
            if(node.selected == 1){
                category.add(node.category)
            }
        }
        return MoodChartItem(id, time.getTime(), rating, descriptionText.text.toString(), category)
    }

    //保存心情节点
    private fun save(){
        val newNode = getNewNode()
        MoodDataGet.addNode(newNode)
        Utils.makeToast(this,"添加成功")

//        Log.d("id:" , newNode.id.toString() )
//        Log.d("time:" , newNode.date.toString() )
//        Log.d("rating:" , newNode.rating.toString())
//        Log.d("event:" , newNode.event )
//        Log.d("category:" , newNode.category[newNode.category.lastIndex] )
        finish()
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
//        val rating = parent?.getItemAtPosition(position).toString()
        rating = parent?.getItemAtPosition(position).toString().toInt()
        Utils.makeToast(this, "选择Rating: $rating")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Utils.makeToast(this, "nothing select!")
    }


}