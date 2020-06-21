package com.xently.payment.ui.square

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog.*
import com.google.android.material.snackbar.Snackbar
import com.xently.payment.R
import com.xently.payment.data.model.Money
import com.xently.payment.data.model.square.Payment
import com.xently.payment.databinding.SquareFragmentBinding
import com.xently.payment.ui.BaseFragment
import com.xently.payment.utils.Log
import com.xently.payment.utils.web.TaskResult
import com.xently.payment.utils.web.errorMessage
import com.xently.xui.utils.ui.fragment.setupToolbar
import com.xently.xui.utils.ui.fragment.showSnackBar
import com.xently.xui.utils.ui.view.setClickListener
import dagger.hilt.android.AndroidEntryPoint
import sqip.Callback
import sqip.CardDetails
import sqip.CardEntry
import sqip.CardEntryActivityResult

@AndroidEntryPoint
class SquareFragment : BaseFragment(), Callback<CardEntryActivityResult> {
    private val args: SquareFragmentArgs by navArgs()
    private val viewModel: SquareViewModel by viewModels()
    private var _binding: SquareFragmentBinding? = null
    private val binding: SquareFragmentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SquareFragmentBinding.inflate(inflater, container, false).apply {
            setupToolbar(toolbar)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activateClickListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.paymentResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TaskResult.Success -> {
                    alertDialog?.run {
                        contentText = getString(R.string.payment_thanks)
                        titleText = getString(R.string.payment_success)
                        setCanceledOnTouchOutside(true)
                        changeAlertType(SUCCESS_TYPE)
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
                    Log.show(LOG_TAG, message, it.error, type = Log.Type.ERROR)
                }
                TaskResult.Loading -> {
                    val title = getString(R.string.processing_payment)
                    showAlertDialog(null, title, PROGRESS_TYPE, false)
                }
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.show(LOG_TAG, "Processing payment results...", type = Log.Type.INFO) // TODO

        CardEntry.handleActivityResult(data, this)
    }

    override fun onResult(result: CardEntryActivityResult) {
        if (result.isSuccess()) {
            val cardResult: CardDetails = result.getSuccessValue()
            val nonce = cardResult.nonce
            viewModel.confirmPayment(Payment(nonce, Money(args.amount, args.currency)))
        } else if (result.isCanceled()) {
            showSnackBar(R.string.payment_cancelled, Snackbar.LENGTH_LONG)
        } else {
            Log.show(LOG_TAG, result.getSuccessValue()) // TODO
        }
    }

    private fun activateClickListeners() {
        with(binding) {
            pay.setClickListener {
                CardEntry.startCardEntryActivity(requireActivity())
            }
        }
    }

    companion object {
        private val LOG_TAG = SquareFragment::class.java.simpleName
    }
}