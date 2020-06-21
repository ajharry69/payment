package com.xently.payment.ui.mpesa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xently.payment.databinding.MpesaFragmentBinding
import com.xently.payment.ui.BaseFragment
import com.xently.xui.utils.ui.fragment.setupToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MpesaFragment : BaseFragment() {

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
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}