package com.xently.payment.ui.mpesa

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xently.payment.data.model.mpesa.Payment
import com.xently.payment.data.repository.mpesa.IMpesaRepository
import com.xently.payment.utils.web.TaskResult
import kotlinx.coroutines.launch

class MpesaViewModel @ViewModelInject constructor(private val repository: IMpesaRepository) :
    ViewModel() {
    private val _lipaNaMpesaResult = MutableLiveData<TaskResult<Any?>>()
    val lipaNaMpesaResult: LiveData<TaskResult<Any?>>
        get() = _lipaNaMpesaResult

    fun startLipaNaMpesa(payment: Payment) {
        _lipaNaMpesaResult.value = TaskResult.Loading
        viewModelScope.launch {
            _lipaNaMpesaResult.value = repository.lipaNaMpesa(payment)
        }
    }
}