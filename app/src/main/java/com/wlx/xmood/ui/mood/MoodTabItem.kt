package com.wlx.xmood.ui.mood

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.wlx.xmood.R

class MoodTabItem(mContext: Context, attr: AttributeSet? = null, def: Int = 0) :
    AppCompatTextView(mContext, attr, def) {

    init {
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
        gravity = Gravity.CENTER
        background = mContext.getDrawable(R.drawable.mood_tab_item_bg)
        this.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply {
            setMargins(0, 10, 0, 10)
        }
    }

    var selectedColor = ContextCompat.getColor(context, R.color.black)
    var defaultColor = ContextCompat.getColor(context, R.color.light_gray)

    override fun setSelected(isSelected: Boolean) {
        super.setSelected(isSelected)
        if (isSelected) {
            this.setTextColor(selectedColor)
        } else {
            this.setTextColor(defaultColor)
        }
    }
}