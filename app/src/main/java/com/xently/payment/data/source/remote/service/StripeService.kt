package com.xently.payment.data.source.remote.service

import com.xently.payment.data.model.Money
import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.utils.web.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StripeService {
    @POST("stripe/client-secret/")
    suspend fun getClientSecret(@Body money: Money): Response<ServerResponse<ClientSecret, Unit>>
}
