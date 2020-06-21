package com.xently.payment.data.source.local

import com.xently.payment.data.model.Money
import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.data.source.IStripeDataSource
import com.xently.payment.utils.web.TaskResult

class StripeLocalDataSource internal constructor() : IStripeDataSource {

    override suspend fun getClientSecret(money: Money) =
        TaskResult.Success(ClientSecret("local_stripe_client_secret"))

}