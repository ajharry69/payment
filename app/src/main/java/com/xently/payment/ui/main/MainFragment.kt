package com.xently.payment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.xently.payment.R
import com.xently.payment.databinding.MainFragmentBinding
import com.xently.xui.Fragment
import com.xently.xui.utils.ui.fragment.setupToolbar
import com.xently.xui.utils.ui.view.addTextChangeListener
import com.xently.xui.utils.ui.view.setClickListener
import com.xently.xui.utils.ui.view.setErrorText
import dagger.hilt.android.AndroidEntryPoint
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
        _binding = MainFragmentBinding.inflate(inflater, container, false).apply {
            toolbar.run {
                (activity as? AppCompatActivity)?.setSupportActionBar(this)
                setupToolbar(this)
            }
            amountContainer.addTextChangeListener()
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

    private fun activateClickListeners() {
        with(binding) {
            stripe.setClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(MainFragmentDirections.stripePay(it))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
            square.setClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(MainFragmentDirections.squarePay(it))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
            braintree.setClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(MainFragmentDirections.braintreePay(it))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
            mpesa.setClickListener { view ->
                amount.text.toString().toFloatOrNull()?.let {
                    view.findNavController().navigate(MainFragmentDirections.mpesaPay(it))
                } ?: amountContainer.setErrorText(R.string.required_field)
            }
        }
    }
}