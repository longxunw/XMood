package com.wlx.xmood.settings.setting.userinfo

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.me.MeDataGet
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.Utils

class UserInfoActivity : BaseActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var isEditted = false
    private lateinit var usernameEdit: EditText
    private lateinit var autographEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val backBtn: ImageButton = findViewById(R.id.user_info_back_btn)
        backBtn.setOnClickListener {
            if (!isEditted) {
                finish()
            } else {
                showBackDialog()
            }
        }
        val saveBtn: ImageView = findViewById(R.id.save_btn)
        saveBtn.setOnClickListener {
            save()
        }
        usernameEdit = findViewById(R.id.user_info_user_text)
        autographEdit = findViewById(R.id.user_info_autograph_text)
        usernameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        autographEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEditted = true
            }
        })
        usernameEdit.setText(MeDataGet.getUserName())
        autographEdit.setText(MeDataGet.getAutograph())
        handler.postDelayed(Runnable {
            isEditted = false
        }, 300)
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

    private fun save() {
        val username = usernameEdit.text.toString()
        val autograph = autographEdit.text.toString()
        if (username.isEmpty()) {
            Utils.makeToast(this, "用户名不能为空")
        } else {
            MeDataGet.setUsername(username)
            MeDataGet.setAutograph(autograph)
            finish()
        }
    }
}