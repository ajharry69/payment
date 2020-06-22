package com.xently.payment.ui.braintree

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.google.android.material.snackbar.Snackbar
import com.xently.payment.R
import com.xently.payment.data.model.braintree.Checkout
import com.xently.payment.data.model.braintree.ClientToken
import com.xently.payment.databinding.BraintreeFragmentBinding
import com.xently.payment.ui.BaseFragment
import com.xently.payment.utils.Log
import com.xently.payment.utils.Log.Type.ERROR
import com.xently.payment.utils.Log.Type.INFO
import com.xently.payment.utils.web.TaskResult
import com.xently.payment.utils.web.TaskResult.Error
import com.xently.payment.utils.web.errorMessage
import com.xently.xui.utils.ui.fragment.setupToolbar
import com.xently.xui.utils.ui.fragment.showSnackBar
import com.xently.xui.utils.ui.view.disableViews
import com.xently.xui.utils.ui.view.enableViews
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BraintreeFragment : BaseFragment() {

    private val args: BraintreeFragmentArgs by navArgs()
    private val viewModel: BraintreeViewModel by viewModels()
    private var _binding: BraintreeFragmentBinding? = null
    private val binding: BraintreeFragmentBinding
        get() = _binding!!
    private var clientToken: ClientToken? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BraintreeFragmentBinding.inflate(inflater, container, false).apply {
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
        viewModel.fetchClientToken()
        viewModel.clientToken.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TaskResult.Success -> {
                    enableViews(binding.pay)
                    clientToken = it.data
                    val message = "Done loading braintree client token"
                    showSnackBar(message)
                    Log.show(LOG_TAG, "${message}: ${clientToken!!.token}", type = INFO)
                }
                is Error -> {
                    enableViews(binding.pay)
                    val message = it.errorMessage
                    showSnackBar(message.toString(), Snackbar.LENGTH_LONG)
                    Log.show(LOG_TAG, message, it.error, type = ERROR)
                }
                TaskResult -> {
                    disableViews(binding.pay)
                    val message = "Loading braintree client token..."
                    showSnackBar(message)
                    Log.show(LOG_TAG, message, type = INFO)
                }
            }
        })
        viewModel.checkoutResult.observe(viewLifecycleOwner, Observer {
            onPaymentResponseReceived(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            var exception: Exception? = null
            when (resultCode) {
                RESULT_OK -> {
                    val result: DropInResult =
                        data!!.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)!!
                    // use the result to update your UI and send the payment method nonce to your server
                    val nonce = result.paymentMethodNonce
                    if (nonce != null) {
                        val checkout = Checkout(nonce.nonce, args.amount, result.deviceData)
                        viewModel.startCheckout(checkout)
                    } else {
                        exception = Exception("Payment method nonce returned nothing")
                    }
                }
                RESULT_CANCELED -> {
                    exception = Exception(getString(R.string.payment_cancelled))
                }
                else -> {
                    exception = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
                }
            }
            exception?.also {
                onPaymentResponseReceived(Error(it))
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun activateClickListeners() {
        binding.pay.setOnClickListener {
            if (clientToken == null) {
                viewModel.fetchClientToken()
                return@setOnClickListener
            }
            val dropInRequest = DropInRequest().clientToken(clientToken!!.token)
            startActivityForResult(dropInRequest.getIntent(requireContext()), REQUEST_CODE)
        }
    }

    companion object {
        private val LOG_TAG = BraintreeFragment::class.java.simpleName
        private const val REQUEST_CODE = 1234
    }
}