package com.xently.payment.data.source.local

import com.xently.payment.data.model.mpesa.Payment
import com.xently.payment.data.source.IMpesaDataSource
import com.xently.payment.utils.web.TaskResult

class MpesaLocalDataSource internal constructor() : IMpesaDataSource {
    override suspend fun lipaNaMpesa(payment: Payment) = TaskResult.Success(null)
}