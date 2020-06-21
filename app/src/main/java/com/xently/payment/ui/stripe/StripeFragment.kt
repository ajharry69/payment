package com.xently.payment.ui.stripe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog.*
import com.google.gson.GsonBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.xently.payment.R
import com.xently.payment.data.model.Money
import com.xently.payment.databinding.StripeFragmentBinding
import com.xently.payment.ui.BaseFragment
import com.xently.payment.utils.Log
import com.xently.payment.utils.Log.Type.ERROR
import com.xently.payment.utils.Log.Type.INFO
import com.xently.payment.utils.isReleaseBuild
import com.xently.payment.utils.web.TaskResult
import com.xently.xui.utils.ui.fragment.setupToolbar
import com.xently.xui.utils.ui.view.setClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StripeFragment : BaseFragment(), ApiResultCallback<PaymentIntentResult> {

    private val args: StripeFragmentArgs by navArgs()
    private val viewModel: StripeViewModel by viewModels()
    private lateinit var stripe: Stripe

    private var _binding: StripeFragmentBinding? = null
    private val binding: StripeFragmentBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stripe = Stripe(requireContext(), PUBLISHABLE_KEY, enableLogging = !isReleaseBuild())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StripeFragmentBinding.inflate(inflater, container, false).apply {
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
        viewModel.clientSecret.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TaskResult.Success -> {
                    binding.cardInput.paymentMethodCreateParams?.let { params ->
                        val confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, it.data.clientSecret)
                        // Send the payment details to Stripe
                        stripe.confirmPayment(this@StripeFragment, confirmParams)
                    }
                }
                is TaskResult.Error -> onError(it.error)
                TaskResult.Loading -> {
                    val title = getString(R.string.processing_payment)
                    showAlertDialog(null, title, PROGRESS_TYPE, false)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSuccess(result: PaymentIntentResult) {
        val paymentIntent = result.intent
        val status = paymentIntent.status
        if (status == StripeIntent.Status.Succeeded) {
            with(GsonBuilder().setPrettyPrinting().create()) {
                alertDialog?.run {
                    titleText = getString(R.string.payment_success)
                    contentText = getString(R.string.payment_thanks)
                    setCanceledOnTouchOutside(true)
                    changeAlertType(SUCCESS_TYPE)
                }
                Log.show(LOG_TAG, toJson(paymentIntent), type = INFO)
            }
        } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
            onError(Exception(paymentIntent.lastPaymentError?.message.orEmpty()))
        }
    }

    override fun onError(e: Exception) {
        alertDialog?.run {
            titleText = getString(R.string.payment_failed)
            contentText = e.message.toString()
            setCanceledOnTouchOutside(true)
            changeAlertType(ERROR_TYPE)
        }
        Log.show(LOG_TAG, e.message, e, type = ERROR)
    }

    private fun activateClickListeners() {
        with(binding) {
            pay.setClickListener {
                viewModel.fetchClientSecret(Money(args.amount, args.currency))
            }
        }
    }

    companion object {
        private val LOG_TAG = StripeFragment::class.java.simpleName
        private const val PUBLISHABLE_KEY =
            "pk_test_51GtNC4LbUZ67PbCUN26R93wKOhSb1uHgORb25u1XogslPAaErugCCTser2Tpx35Vmq99SBXD0hF2pOINm1siNFuz00whhntDdM"
        /*private const val PAYMENT_SUCCESS_TEST_CARD = "4242 4242 4242 4242"
        private const val PAYMENT_AUTH_REQUIRED_TEST_CARD = "4000 0025 0000 3155"
        private const val PAYMENT_DECLINE_TEST_CARD = "4000 0000 0000 9995"*/
    }
}