package com.wlx.xmood.ui.countDown.edit

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.jzxiang.pickerview.data.Type
import com.wlx.xmood.R
import com.wlx.xmood.ui.countDown.CountDownDataGet
import com.wlx.xmood.ui.countDown.CountDownItem
import com.wlx.xmood.ui.daily.DailyDataGet
import com.wlx.xmood.utils.AlarmUtil
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.TimeUtil
import com.wlx.xmood.utils.Utils
import com.wlx.xmood.widget.TimePicker

class CountDownEditActivity : AppCompatActivity() {

    private var isEditted = false
    private var status = 0
    private val ADD = 0
    private val UPDATE = 1
    private val hanlder = Handler(Looper.getMainLooper())
    private var id = -1
    private lateinit var contentText: EditText
    private lateinit var startDay: TimePicker
    private lateinit var title: TextView
    private lateinit var countDownItem: CountDownItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down_edit)

        id = intent.getIntExtra("id", -1)
        contentText = findViewById(R.id.content)
        startDay = findViewById(R.id.startDay)
        title = findViewById(R.id.title)

        contentText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        startDay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        startDay.setType(Type.YEAR_MONTH_DAY)
        startDay.setOnClickListener {
            startDay.pickerBuilder.setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(startDay.current).build().show(supportFragmentManager,"起始时间")
        }

        if (id == -1) {
            title.text = "添加倒计时"
            status = ADD
        } else {
            title.text = "编辑倒计时"
            countDownItem = CountDownDataGet.getCountDownById(id)
            status = UPDATE
            countDownItem.let {
                contentText.setText(countDownItem.content)
                startDay.setTime(countDownItem.startDay)
            }
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.memorandum_edit_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_delete ->{
                    showDeleteDialog()
                }
                R.id.edit_save -> {
                    save()
                }
            }
            true
        }

        val backBtn: ImageButton = findViewById(R.id.back)
        backBtn.setOnClickListener {
            if (isEditted) {
                showBackDialog()
            }else{
                finish()
            }
        }
        hanlder.postDelayed(Runnable {
            isEditted = false
        },300)
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
                bottomDialog.dismiss()
                finish()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.preserve_and_exit)
            ?.setOnClickListener {
                save()
                bottomDialog.dismiss()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.cancel)
            ?.setOnClickListener {
                bottomDialog.dismiss()
            }
        bottomDialog.show()
    }

    private fun showDeleteDialog() {
        val bottomDialog = Dialog(this, R.style.MyDialogTheme);
        val contentView = LayoutInflater.from(this).inflate(
            R.layout.delete_memorandum_dialog_content,
            null
        )
        bottomDialog.setContentView(contentView)
        val layoutParams: ViewGroup.MarginLayoutParams =
            contentView.layoutParams as ViewGroup.MarginLayoutParams;
        layoutParams.width =
            this.resources.displayMetrics.widthPixels - DensityUtil.dp2px(this, 40f)
        contentView.layoutParams = layoutParams
        bottomDialog.window?.setGravity(Gravity.CENTER)
        bottomDialog.window?.findViewById<TextView>(R.id.confirm_delete)
            ?.setOnClickListener {
                CountDownDataGet.deleteCountDown(id)
                Utils.makeToast(this, "已删除")
                bottomDialog.dismiss()
                finish()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.cancel)
            ?.setOnClickListener {
                bottomDialog.dismiss()
            }
        bottomDialog.show()
    }

    private fun save() {
        val newCountDownItem = getNewCountDown()
        if (newCountDownItem.content.isEmpty()) {
            Utils.makeToast(this, "内容不能为空")
            return
        }
        if (newCountDownItem.startDay == 0L) {
            Utils.makeToast(this, "日期不能为空")
            return
        }

        val result = CountDownDataGet.addCountDown(newCountDownItem)
        Utils.makeToast(this, "已保存")
        finish()
    }

    private fun getNewCountDown(): CountDownItem {
        return CountDownItem(
            id,
            contentText.text.toString(),
            startDay.getTime()
        )
    }
}