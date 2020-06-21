package com.xently.payment.data.repository.square

import com.xently.payment.data.model.square.Payment
import com.xently.payment.data.source.ISquareDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SquareRepository internal constructor(
    private val local: ISquareDataSource,
    private val remote: ISquareDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISquareRepository {
    override suspend fun confirmPayment(payment: Payment) = withContext(ioDispatcher) {
        remote.confirmPayment(payment)
    }
}