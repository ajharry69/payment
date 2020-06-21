package com.xently.payment.ui

import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.ERROR_TYPE
import com.xently.payment.R
import com.xently.xui.Fragment
import com.xently.xui.utils.getThemedColor

open class BaseFragment : Fragment() {
    protected var alertDialog: SweetAlertDialog? = null

    fun showAlertDialog(
        content: String?,
        title: String? = null,
        type: Int = ERROR_TYPE,
        cancelable: Boolean = true
    ) {
        alertDialog = SweetAlertDialog(requireContext(), type).apply {
            titleText = title
            if (type == SweetAlertDialog.PROGRESS_TYPE) {
                progressHelper.barColor = requireContext().getThemedColor(R.attr.colorAccent)
            } else contentText = content
            setCancelable(cancelable)
            setCanceledOnTouchOutside(cancelable)
        }
        alertDialog!!.show()
    }
}