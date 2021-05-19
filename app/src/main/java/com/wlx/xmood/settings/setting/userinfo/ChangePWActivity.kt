package com.wlx.xmood.settings.setting.userinfo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.wlx.xmood.R
import com.wlx.xmood.utils.Utils

class ChangePWActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_p_w)
        val backBtn: ImageButton = findViewById(R.id.change_pw_back_btn)
        backBtn.setOnClickListener {
            finish()
        }
        val currentPwEdit: EditText = findViewById(R.id.change_pw_current_pw_edit)
        val newPwEdit: EditText = findViewById(R.id.change_pw_new_pw_edit)
        val newPwRepeatEdit: EditText = findViewById(R.id.change_pw_new_pw_repeat_edit)
        val confirmBtn: Button = findViewById(R.id.change_pw_confirm_btn)
        confirmBtn.setOnClickListener {
            val currentPw = currentPwEdit.text.toString()
            val newPw = newPwEdit.text.toString()
            val newPwRepeat = newPwRepeatEdit.text.toString()
            if (currentPw.isEmpty() or newPw.isEmpty() or newPwRepeat.isEmpty()) {
                Utils.makeToast(this, "输入框不能为空")
            } else if (newPw == newPwRepeat) {
                val result = changePw(currentPw, newPw)
                if (result) {
                    Utils.makeToast(this, "密码修改成功")
                    finish()
                } else {
                    Utils.makeToast(this, "当前密码错误")
                }
            } else {
                Utils.makeToast(this, "新密码与确认密码不一致，请重新输入")
            }
        }
    }

    private fun changePw(currentPw: String, newPw: String): Boolean {
        return true
    }

}