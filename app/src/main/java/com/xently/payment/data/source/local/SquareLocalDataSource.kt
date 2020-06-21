package com.xently.payment.data.source.local

import com.xently.payment.data.model.square.Payment
import com.xently.payment.data.source.ISquareDataSource
import com.xently.payment.utils.web.TaskResult

class SquareLocalDataSource internal constructor() : ISquareDataSource {
    override suspend fun confirmPayment(payment: Payment) = TaskResult.Success(Unit)
}