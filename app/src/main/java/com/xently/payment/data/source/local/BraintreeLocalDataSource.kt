package com.xently.payment.data.source.local

import com.xently.payment.data.model.braintree.Checkout
import com.xently.payment.data.model.braintree.ClientToken
import com.xently.payment.data.source.IBraintreeDataSource
import com.xently.payment.utils.web.TaskResult

class BraintreeLocalDataSource internal constructor() : IBraintreeDataSource {
    override suspend fun getClientToken() = TaskResult.Success(ClientToken("local-token"))

    override suspend fun checkout(checkout: Checkout) = TaskResult.Success(Any())
}