package com.xently.payment.data.model.braintree

data class Checkout(
    val nonce: String,
    val amount: Float,
    val deviceData: String? = null
)