package com.xently.payment.ui.mpesa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.xently.payment.R
import com.xently.payment.data.model.mpesa.Payment
import com.xently.payment.databinding.MpesaFragmentBinding
import com.xently.payment.ui.BaseFragment
import com.xently.xui.utils.ui.fragment.setupToolbar
import com.xently.xui.utils.ui.view.addTextChangeListener
import com.xently.xui.utils.ui.view.setErrorText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MpesaFragment : BaseFragment() {

    private val args: MpesaFragmentArgs by navArgs()
    private val viewModel: MpesaViewModel by viewModels()
    private var _binding: MpesaFragmentBinding? = null
    private val binding: MpesaFragmentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MpesaFragmentBinding.inflate(inflater, container, false).apply {
            setupToolbar(toolbar)
            mobileNumberContainer.addTextChangeListener()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activateClickListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.lipaNaMpesaResult.observe(viewLifecycleOwner, Observer {
            onPaymentResponseReceived(it)
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun activateClickListeners() {
        with(binding) {
            pay.setOnClickListener {
                val mobileNumber: String? = mobileNumber.text.toString()
                if (mobileNumber.isNullOrBlank()) {
                    mobileNumberContainer.setErrorText(R.string.required_field)
                    return@setOnClickListener
                }
                viewModel.startLipaNaMpesa(Payment(mobileNumber, args.amount))
            }
        }
    }

}