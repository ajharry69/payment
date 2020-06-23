package com.xently.payment.data.source.remote

import com.xently.payment.WebServiceBuilder.getService
import com.xently.payment.WebServiceBuilder.sendWebRequest
import com.xently.payment.data.model.mpesa.Payment
import com.xently.payment.data.source.IMpesaDataSource
import com.xently.payment.data.source.remote.service.MpesaService

class MpesaRemoteDataSource internal constructor() : IMpesaDataSource {
    private val webService = getService(MpesaService::class.java)
    override suspend fun lipaNaMpesa(payment: Payment) = sendWebRequest(suspend {
        webService.lipaNaMpesa(payment)
    })
}