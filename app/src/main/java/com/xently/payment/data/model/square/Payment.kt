package com.xently.payment.data.model.square

import com.xently.payment.data.model.Money

data class Payment(
    val nonce: String,
    val amountMoney: Money? = null,
    val appFeeMoney: Money? = null,
    val production: Boolean = false,
    val autocomplete: Boolean = true
)