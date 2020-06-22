package com.xently.payment.data.repository.braintree

import com.xently.payment.data.model.braintree.Checkout
import com.xently.payment.data.source.IBraintreeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BraintreeRepository internal constructor(
    private val local: IBraintreeDataSource,
    private val remote: IBraintreeDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IBraintreeRepository {
    override suspend fun getClientToken() = withContext(ioDispatcher) {
        remote.getClientToken()
    }

    override suspend fun checkout(checkout: Checkout) = withContext(ioDispatcher) {
        remote.checkout(checkout)
    }
}