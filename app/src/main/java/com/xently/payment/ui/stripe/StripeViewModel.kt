package com.xently.payment.ui.stripe

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xently.payment.data.model.Money
import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.data.repository.stripe.IStripeRepository
import com.xently.payment.utils.web.TaskResult
import kotlinx.coroutines.launch

class StripeViewModel @ViewModelInject constructor(private val repository: IStripeRepository) :
    ViewModel() {

    private val _clientSecret = MutableLiveData<TaskResult<ClientSecret>>()
    val clientSecret: LiveData<TaskResult<ClientSecret>>
        get() = _clientSecret

    fun fetchClientSecret(money: Money) {
        _clientSecret.value = TaskResult.Loading
        viewModelScope.launch {
            _clientSecret.value = repository.getClientSecret(money)
        }
    }
}