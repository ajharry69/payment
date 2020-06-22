package com.xently.payment.data.source.remote.service

import com.xently.payment.data.model.braintree.Checkout
import com.xently.payment.data.model.braintree.ClientToken
import com.xently.payment.utils.web.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BraintreeService {
    @GET("braintree/client-token/")
    suspend fun getClientToken(): Response<ServerResponse<ClientToken, Unit>>

    @POST("braintree/checkout/")
    suspend fun checkout(@Body checkout: Checkout): Response<ServerResponse<Any?, Unit>>
}