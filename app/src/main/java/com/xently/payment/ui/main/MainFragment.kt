package com.xently.payment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.xently.payment.R
import com.xently.payment.databinding.MainFragmentBinding
import com.xently.payment.ui.main.MainFragmentDirections.Companion.braintreePay
import com.xently.payment.ui.main.MainFragmentDirections.Companion.mpesaPay
import com.xently.payment.ui.main.MainFragmentDirections.Companion.squarePay
import com.xently.payment.ui.main.MainFragmentDirections.Companion.stripePay
import com.xently.xui.Fragment
import com.xently.xui.utils.ui.fragment.setupToolbar
import com.xently.xui.utils.ui.view.addTextChangeListener
import com.xently.xui.utils.ui.view.setErrorText
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currencies = Currency.getAvailableCurrencies()
            .map { it.currencyCode.toUpperCase(Locale.getDefault()) }
        val adapter = ArrayAdapter(requireContext(), R.layout.currency_menu_item, currencies)
        _binding = MainFragmentBinding.inflate(inflater, container, false).apply {
            toolbar.run {
                (activity as? AppCompatActivity)?.setSupportActionBar(this)
                setupToolbar(this)
            }
            currency.run {
                setAdapter(adapter)
                setText(Currency.getInstance(Locale.US).currencyCode, true)
            }
            amountContainer.addTextChangeListener()
            currencyContainer.addTextChangeListener()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activateClickListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun getSelectedCurrency(): String {
        return binding.currency.text.run {
            if (isNullOrBlank()) getString(R.string.default_currency) else toString()
        }
    }

    private fun activateClickListeners() {
        with(binding) {
            stripe.setOnClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(stripePay(it, getSelectedCurrency()))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
            square.setOnClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(squarePay(it, getSelectedCurrency()))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
            braintree.setOnClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(braintreePay(it, getSelectedCurrency()))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
            mpesa.setOnClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(mpesaPay(it, getSelectedCurrency()))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
        }
    }
}