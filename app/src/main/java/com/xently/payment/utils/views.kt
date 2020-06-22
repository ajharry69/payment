package com.xently.payment.utils

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.xently.payment.R
import com.xently.xui.utils.ui.view.hideKeyboard


open class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {
    override fun setOnClickListener(l: OnClickListener?) {
        hideKeyboard(this)
        super.setOnClickListener(l)
    }
}