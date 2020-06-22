package com.xently.payment.ui.braintree

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xently.payment.data.model.braintree.Checkout
import com.xently.payment.data.model.braintree.ClientToken
import com.xently.payment.data.repository.braintree.IBraintreeRepository
import com.xently.payment.utils.web.TaskResult
import kotlinx.coroutines.launch

class BraintreeViewModel @ViewModelInject constructor(private val repository: IBraintreeRepository) :
    ViewModel() {
    private val _clientToken = MutableLiveData<TaskResult<ClientToken>>()
    val clientToken: LiveData<TaskResult<ClientToken>>
        get() = _clientToken
    private val _checkoutResult = MutableLiveData<TaskResult<Any?>>()
    val checkoutResult: LiveData<TaskResult<Any?>>
        get() = _checkoutResult

    fun fetchClientToken() {
        _clientToken.value = TaskResult.Loading
        viewModelScope.launch {
            _clientToken.value = repository.getClientToken()
        }
    }

    fun startCheckout(checkout: Checkout) {
        _checkoutResult.value = TaskResult.Loading
        viewModelScope.launch {
            _checkoutResult.value = repository.checkout(checkout)
        }
    }

}