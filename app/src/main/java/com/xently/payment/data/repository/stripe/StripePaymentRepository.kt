package com.xently.payment.data.repository.stripe

import com.xently.payment.data.source.IStripePaymentDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StripePaymentRepository internal constructor(
    private val local: IStripePaymentDataSource,
    private val remote: IStripePaymentDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IStripePaymentRepository {
    override suspend fun getClientSecret(amount: Float) = withContext(ioDispatcher) {
        remote.getClientSecret(amount)
    }
}