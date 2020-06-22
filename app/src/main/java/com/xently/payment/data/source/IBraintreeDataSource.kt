package com.xently.payment.data.source

import com.xently.payment.data.model.braintree.Checkout
import com.xently.payment.data.model.braintree.ClientToken
import com.xently.payment.utils.web.TaskResult

interface IBraintreeDataSource {
    suspend fun getClientToken(): TaskResult<ClientToken>
    suspend fun checkout(checkout: Checkout): TaskResult<Any?>
}