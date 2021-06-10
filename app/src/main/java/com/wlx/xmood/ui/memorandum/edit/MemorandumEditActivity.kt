package com.wlx.xmood.ui.memorandum.edit

import android.app.Dialog
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
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.memorandum.MemoDataGet
import com.wlx.xmood.ui.memorandum.MemorandumItem
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.Utils
import java.util.*

class MemorandumEditActivity : BaseActivity() {
    private var isNew = true
    private var id = -1
    private lateinit var titleText: EditText
    private lateinit var bodyText: EditText
    private lateinit var catalogText: EditText
    private var isEditted = false
    private val handler = Handler(Looper.getMainLooper())
    private val TAG = "MemorandumEditActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorandum_edit)
        id = intent.getIntExtra("id", -1)
        titleText = findViewById(R.id.memorandum_edit_title)
        bodyText = findViewById(R.id.memorandum_edit_body)
        catalogText = findViewById(R.id.memorandum_edit_note_catalog)
        if (id == -1) {
            isNew = true
            val mcatalog = intent.getStringExtra("catalog") ?: "__all"
            if(mcatalog!="__all"){
                catalogText.setText(mcatalog)
            }
        } else {
            isNew = false
            val memo = MemoDataGet.getNoteById(id)
            if (memo != null) {
                titleText.setText(memo.head)
                bodyText.setText(memo.body)
                catalogText.setText(memo.catalog)
            }
        }
        val backBtn: ImageView = findViewById(R.id.memorandum_edit_back)
        val toolbar: Toolbar = findViewById(R.id.memorandum_edit_toolbar)
        toolbar.inflateMenu(R.menu.memorandum_edit_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_save -> {
                    save()
                }
                R.id.edit_delete -> {
                    showDeleteDialog()
                }
            }
            true
        }
        titleText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        bodyText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        catalogText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })

        backBtn.setOnClickListener {
            if ((isNew and isEmpty()) or !isEditted) {
                finish()
            } else {
                showBackDialog()
            }
        }
        handler.postDelayed(Runnable {
            isEditted = false
        }, 300)
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
                bottomDialog.dismiss()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.select_schedule_bg_cancel)
            ?.setOnClickListener {
                bottomDialog.dismiss()
            }
        bottomDialog.show()
    }

    private fun getNewNote(): MemorandumItem {
        return MemorandumItem(
            id,
            titleText.text.toString(),
            bodyText.text.toString(),
            Date(),
            catalogText.text.toString()
        )
    }

    private fun save() {
        val newNote = getNewNote()
        if (isEmpty()) {
            Utils.makeToast(this, "空备忘录无法保存")
            return
        }
        Log.d(TAG, "save: 111")
        MemoDataGet.addNote(newNote)
        Utils.makeToast(this, "已保存")
        finish()
    }

    private fun isEmpty(): Boolean {
        return titleText.text.toString().isEmpty() and
                bodyText.text.toString().isEmpty() and
                catalogText.text.toString().isEmpty()
    }
}