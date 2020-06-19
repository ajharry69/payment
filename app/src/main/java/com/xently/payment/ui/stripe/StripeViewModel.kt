package com.xently.payment.ui.stripe

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.data.repository.stripe.IStripePaymentRepository
import com.xently.payment.utils.web.TaskResult

class StripeViewModel @ViewModelInject constructor(private val repository: IStripePaymentRepository) :
    ViewModel() {

    private val amount = MutableLiveData<Float>()

    fun setAmount(amount: Float) {
        this.amount.value = amount
    }

    fun getClientSecret(): LiveData<TaskResult<ClientSecret>> = Transformations.switchMap(amount) {
        liveData { emit(repository.getClientSecret(it)) }
    }
}