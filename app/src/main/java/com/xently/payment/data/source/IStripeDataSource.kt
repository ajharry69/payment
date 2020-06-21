package com.xently.payment.data.source

import com.xently.payment.data.model.Money
import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.utils.web.TaskResult

interface IStripeDataSource {
    suspend fun getClientSecret(money: Money): TaskResult<ClientSecret>
}