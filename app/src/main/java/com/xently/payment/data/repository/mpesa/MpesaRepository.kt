package com.xently.payment.data.repository.mpesa

import com.xently.payment.data.model.mpesa.Payment
import com.xently.payment.data.source.IMpesaDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MpesaRepository internal constructor(
    private val local: IMpesaDataSource,
    private val remote: IMpesaDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IMpesaRepository {
    override suspend fun lipaNaMpesa(payment: Payment) = withContext(ioDispatcher) {
        remote.lipaNaMpesa(payment)
    }
}