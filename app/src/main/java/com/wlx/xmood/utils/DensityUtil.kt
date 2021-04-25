package com.wlx.xmood.utils

import android.content.Context
import android.util.TypedValue


object DensityUtil {
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpVal,
            context.resources.displayMetrics
        ).toInt()
    }

    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, spVal,
            context.resources.displayMetrics
        ).toInt()
    }

    fun px2dp(context: Context, pxVal: Float): Float {
        val scale: Float = context.resources.displayMetrics.density
        return pxVal / scale
    }

    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }
}