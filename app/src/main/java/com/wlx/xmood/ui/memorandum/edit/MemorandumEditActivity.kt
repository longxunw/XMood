package com.wlx.xmood.ui.memorandum.edit

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.MemoDataGet
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.Utils

class MemorandumEditActivity : BaseActivity() {
    private var isNew = true
    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorandum_edit)
        id = intent.getIntExtra("id", -1)
        if (id == -1) {
            isNew = true
        } else {
            isNew = false
            val titleText: EditText = findViewById(R.id.memorandum_edit_title)
            val bodyText: EditText = findViewById(R.id.memorandum_edit_body)
            val memo = MemoDataGet.getNoteById(id)
            if (memo != null) {
                titleText.setText(memo.head)
                bodyText.setText(memo.body)
            }
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
                R.id.edit_delete -> {
                    showDialog(id, "")
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

    //加入无用temp参数是为了规避莫名奇妙的override需要
    private fun showDialog(id: Int, temp: String) {
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
//        layoutParams.bottomMargin = DensityUtil.dp2px(this, 16f)
        contentView.layoutParams = layoutParams
        bottomDialog.window?.setGravity(Gravity.CENTER)
//        bottomDialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog.window?.findViewById<TextView>(R.id.confirm_delete)
            ?.setOnClickListener {
                MemoDataGet.deleteNote(id)
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