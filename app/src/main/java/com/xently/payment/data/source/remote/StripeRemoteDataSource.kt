package com.xently.payment.data.source.remote

import com.xently.payment.WebServiceBuilder
import com.xently.payment.WebServiceBuilder.sendWebRequest
import com.xently.payment.data.model.Money
import com.xently.payment.data.source.IStripeDataSource
import com.xently.payment.data.source.remote.service.StripeService

class StripeRemoteDataSource internal constructor() : IStripeDataSource {
    private val webService = WebServiceBuilder.getService(StripeService::class.java)

    override suspend fun getClientSecret(money: Money) = sendWebRequest(suspend {
        webService.getClientSecret(money)
    })

}