package com.xently.payment.data.source

import com.xently.payment.data.model.square.Payment
import com.xently.payment.utils.web.TaskResult

interface ISquareDataSource {
    suspend fun confirmPayment(payment: Payment): TaskResult<Unit>
}