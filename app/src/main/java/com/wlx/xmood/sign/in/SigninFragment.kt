package com.wlx.xmood.sign.`in`

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wlx.xmood.MainActivity
import com.wlx.xmood.R

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
        val btn: Button = root.findViewById(R.id.signinBtn)
        btn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            Log.d(TAG, "onCreateView: fawefa")
            startActivity(intent)
        }
        return root
    }

}