package com.xently.payment.data.source.remote.service

import com.xently.payment.data.model.stripe.ClientSecret
import com.xently.payment.utils.web.ServerResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface StripePaymentService {
    @FormUrlEncoded
    @POST("stripe/client-secret/")
    suspend fun getClientSecret(@Field("amount") amount: Float): Response<ServerResponse<ClientSecret, Unit>>
}
