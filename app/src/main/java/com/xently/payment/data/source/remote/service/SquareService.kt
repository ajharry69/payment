package com.xently.payment.data.source.remote.service

import com.xently.payment.data.model.square.Payment
import com.xently.payment.data.model.square.SquareError
import com.xently.payment.utils.web.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SquareService {
    @POST("square/confirm-payment/")
    suspend fun confirmPayment(@Body payment: Payment): Response<ServerResponse<Unit, List<SquareError>>>
}