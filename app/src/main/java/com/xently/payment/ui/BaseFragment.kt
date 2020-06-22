package com.xently.payment.ui

import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.ERROR_TYPE
import com.xently.payment.R
import com.xently.payment.utils.Log
import com.xently.payment.utils.web.TaskResult
import com.xently.payment.utils.web.errorMessage
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

    open fun onPaymentResponseReceived(it: TaskResult<Any?>?) {
        when (it) {
            is TaskResult.Success -> {
                alertDialog?.run {
                    contentText = getString(R.string.payment_thanks)
                    titleText = getString(R.string.payment_success)
                    setCanceledOnTouchOutside(true)
                    changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                }
            }
            is TaskResult.Error -> {
                val message = it.errorMessage.toString()
                alertDialog?.run {
                    contentText = message
                    titleText = getString(R.string.payment_failed)
                    setCanceledOnTouchOutside(true)
                    changeAlertType(ERROR_TYPE)
                }
                Log.show(this::class.java.simpleName, message, it.error, type = Log.Type.ERROR)
            }
            TaskResult -> {
                val title = getString(R.string.processing_payment)
                showAlertDialog(null, title, SweetAlertDialog.PROGRESS_TYPE, false)
            }
        }
    }
}