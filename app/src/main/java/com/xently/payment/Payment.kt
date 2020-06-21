package com.xently.payment

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import sqip.CardEntry
import sqip.CardNonceBackgroundHandler
import javax.inject.Inject

@HiltAndroidApp
class Payment : Application() {
    @Inject
    lateinit var cardNonceHandler: CardNonceBackgroundHandler

    override fun onCreate() {
        super.onCreate()
        CardEntry.setCardNonceBackgroundHandler(cardNonceHandler)
    }
}