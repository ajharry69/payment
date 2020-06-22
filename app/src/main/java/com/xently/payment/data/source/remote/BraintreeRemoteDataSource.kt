package com.xently.payment.data.source.remote

import com.xently.payment.WebServiceBuilder
import com.xently.payment.WebServiceBuilder.sendWebRequest
import com.xently.payment.data.model.braintree.Checkout
import com.xently.payment.data.source.IBraintreeDataSource
import com.xently.payment.data.source.remote.service.BraintreeService

class BraintreeRemoteDataSource internal constructor() : IBraintreeDataSource {
    private val webService = WebServiceBuilder.getService(BraintreeService::class.java)
    override suspend fun getClientToken() = sendWebRequest(suspend {
        webService.getClientToken()
    })

    override suspend fun checkout(checkout: Checkout) = sendWebRequest(suspend {
        webService.checkout(checkout)
    })
}