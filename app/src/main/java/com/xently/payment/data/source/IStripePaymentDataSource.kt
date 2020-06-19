package com.xently.payment.data.source

import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.utils.web.TaskResult

interface IStripePaymentDataSource {
    suspend fun getClientSecret(amount: Float): TaskResult<ClientSecret>
}