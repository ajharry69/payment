package com.xently.payment.data.source.remote

import com.xently.payment.WebServiceBuilder
import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.data.source.remote.service.StripePaymentService
import com.xently.payment.data.source.IStripePaymentDataSource
import com.xently.payment.utils.web.TaskResult

class StripePaymentRemoteDataSource internal constructor() :
    IStripePaymentDataSource {
    private val webService = WebServiceBuilder.getService(StripePaymentService::class.java)

    override suspend fun getClientSecret(amount: Float): TaskResult<ClientSecret> {
        return WebServiceBuilder.sendWebRequest(suspend {
            webService.getClientSecret(amount)
        })
    }

}