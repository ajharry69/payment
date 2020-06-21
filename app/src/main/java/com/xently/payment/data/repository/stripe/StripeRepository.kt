package com.xently.payment.data.repository.stripe

import com.xently.payment.data.model.Money
import com.xently.payment.data.source.IStripeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StripeRepository internal constructor(
    private val local: IStripeDataSource,
    private val remote: IStripeDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IStripeRepository {
    override suspend fun getClientSecret(money: Money) = withContext(ioDispatcher) {
        remote.getClientSecret(money)
    }
}