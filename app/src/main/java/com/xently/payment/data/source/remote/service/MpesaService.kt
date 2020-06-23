package com.xently.payment.data.source.remote.service

import com.xently.payment.data.model.mpesa.Payment
import com.xently.payment.utils.web.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MpesaService {
    @POST("mpesa/lnm/")
    suspend fun lipaNaMpesa(@Body payment: Payment): Response<ServerResponse<Any?, Unit>>
}