package com.wlx.xmood.sign.`in`

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
import com.wlx.xmood.sign.up.SignupFragment
import com.wlx.xmood.utils.Utils

class SigninFragment : Fragment() {

    companion object {
        fun newInstance() = SigninFragment()
    }

    private val TAG = "SigninFragment"
    private lateinit var viewModel: SigninViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SigninViewModel::class.java)
        val root = inflater.inflate(R.layout.signin_fragment, container, false)
        val signinBtn: Button = root.findViewById(R.id.signinInBtn)
        signinBtn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        val signupBtn: Button = root.findViewById(R.id.signinUpBtn)
        signupBtn.setOnClickListener {
            Utils.replaceFragmentToActivity(
                fragmentManager, SignupFragment.newInstance(), R.id.signFragment
            )
        }
        return root
    }

}