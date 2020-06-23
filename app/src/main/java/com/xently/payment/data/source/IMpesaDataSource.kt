package com.xently.payment.data.source

import com.xently.payment.data.model.mpesa.Payment
import com.xently.payment.utils.web.TaskResult

interface IMpesaDataSource {
    suspend fun lipaNaMpesa(payment: Payment): TaskResult<Any?>
}