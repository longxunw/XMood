package com.wlx.xmood.ui.memorandum.edit

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.wlx.xmood.R
import com.wlx.xmood.utils.Utils

class MemorandumEditActivity : AppCompatActivity() {
    private var isNew = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorandum_edit)
        val title = intent.getStringExtra("title")
        if (title == null) {
            isNew = true
        } else {
            isNew = false
            val titleText: TextView = findViewById(R.id.memorandum_edit_title)
            titleText.text = title
        }
        val backBtn: ImageView = findViewById(R.id.memorandum_edit_back)
        val toolbar: Toolbar = findViewById(R.id.memorandum_edit_toolbar)
        toolbar.inflateMenu(R.menu.memorandum_edit_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_save -> {
                    save()
                    Utils.makeToast(this, "已保存")
                }
            }
            true
        }
        backBtn.setOnClickListener {
            save()
            Utils.makeToast(this, "已保存")
            finish()
        }

    }

//    private fun showDialog() {
//        val bottomDialog = Dialog(this, R.style.MyDialogTheme);
//        val contentView = LayoutInflater.from(this).inflate(
//            R.layout.exit_edit_dialog_content,
//            null
//        )
//        bottomDialog.setContentView(contentView)
//        val layoutParams: ViewGroup.MarginLayoutParams =
//            contentView.layoutParams as ViewGroup.MarginLayoutParams;
//        layoutParams.width =
//            resources.displayMetrics.widthPixels - DensityUtil.dp2px(this, 16f)
//        layoutParams.bottomMargin = DensityUtil.dp2px(this, 16f)
//        contentView.layoutParams = layoutParams
//        bottomDialog.window?.setGravity(Gravity.BOTTOM);
//        bottomDialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
//        bottomDialog.window?.findViewById<TextView>(R.id.direct_exit)
//            ?.setOnClickListener {
//                finish()
//                bottomDialog.dismiss()
//            }
//        bottomDialog.window?.findViewById<TextView>(R.id.preserve_and_exit)
//            ?.setOnClickListener {
//                save()
//                Utils.makeToast(this, "已保存")
//                finish()
//                bottomDialog.dismiss()
//            }
//        bottomDialog.window?.findViewById<TextView>(R.id.select_schedule_bg_cancel)
//            ?.setOnClickListener {
//                bottomDialog.dismiss()
//            }
//        bottomDialog.show()
//    }
//
//    override fun onBackPressed() {
//        showDialog()
//    }

    private fun save() {

    }
}