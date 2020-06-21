package com.xently.payment.data.source.remote

import com.xently.payment.WebServiceBuilder
import com.xently.payment.WebServiceBuilder.sendWebRequest
import com.xently.payment.data.model.square.Payment
import com.xently.payment.data.source.ISquareDataSource
import com.xently.payment.data.source.remote.service.SquareService

class SquareRemoteDataSource internal constructor() : ISquareDataSource {
    private val webService = WebServiceBuilder.getService(SquareService::class.java)

    override suspend fun confirmPayment(payment: Payment) = sendWebRequest(suspend {
        webService.confirmPayment(payment)
    })
}