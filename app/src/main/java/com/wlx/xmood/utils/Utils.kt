package com.wlx.xmood.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object Utils {
    fun replaceFragmentToActivity(
        manager: FragmentManager?,
        fragment: Fragment,
        frameId: Int
    ) {
        manager?.beginTransaction()?.apply {
            replace(frameId, fragment)
            addToBackStack(null)
        }?.commit()
    }

    fun makeToast(context: Context, string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

}