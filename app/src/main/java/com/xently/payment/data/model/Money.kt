package com.xently.payment.data.model

import java.util.*

data class Money(val amount: Float, val currency: String = Currency.getInstance(Locale.US).currencyCode)