package com.xently.payment.data.source.local

import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.data.source.IStripePaymentDataSource
import com.xently.payment.utils.web.TaskResult

class StripePaymentLocalDataSource internal constructor() :
    IStripePaymentDataSource {

    override suspend fun getClientSecret(amount: Float): TaskResult<ClientSecret> {
        return TaskResult.Success(
            ClientSecret(
                "local_stripe_client_secret"
            )
        )
    }

}