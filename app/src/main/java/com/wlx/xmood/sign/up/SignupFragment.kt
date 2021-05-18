package com.wlx.xmood.sign.up

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wlx.xmood.MainActivity
import com.wlx.xmood.R
import com.wlx.xmood.sign.`in`.SigninFragment
import com.wlx.xmood.utils.Utils

class SignupFragment : Fragment() {

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        val root = inflater.inflate(R.layout.signup_fragment, container, false)
        val signinBtn: Button = root.findViewById(R.id.signupInBtn)
        signinBtn.setOnClickListener {
            Utils.replaceFragmentToActivity(
                fragmentManager, SigninFragment.newInstance(), R.id.signFragment
            )
        }
        val signupBtn: Button = root.findViewById(R.id.signupUpBtn)
        signupBtn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        return root
    }

}