package com.xently.payment.ui.square

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xently.payment.data.model.square.Payment
import com.xently.payment.data.repository.square.ISquareRepository
import com.xently.payment.utils.web.TaskResult
import kotlinx.coroutines.launch

class SquareViewModel @ViewModelInject constructor(private val repository: ISquareRepository) :
    ViewModel() {
    private val _paymentResult = MutableLiveData<TaskResult<Unit>>()
    val paymentResult: LiveData<TaskResult<Unit>>
        get() = _paymentResult

    fun confirmPayment(payment: Payment) {
        _paymentResult.value = TaskResult.Loading
        viewModelScope.launch {
            _paymentResult.value = repository.confirmPayment(payment)
        }
    }
}