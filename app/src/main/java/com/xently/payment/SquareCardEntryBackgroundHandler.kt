package com.xently.payment

import android.content.Context
import com.xently.payment.data.model.Money
import com.xently.payment.data.model.square.Payment
import com.xently.payment.data.repository.square.ISquareRepository
import com.xently.payment.utils.web.TaskResult
import com.xently.payment.utils.web.errorMessage
import com.xently.payment.utils.web.isSuccessful
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import sqip.CardDetails
import sqip.CardEntryActivityCommand
import sqip.CardNonceBackgroundHandler
import java.io.IOException
import javax.inject.Inject

class SquareCardEntryBackgroundHandler @Inject internal constructor(
    @ApplicationContext private val context: Context,
    private val repository: ISquareRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CardNonceBackgroundHandler {
    override fun handleEnteredCardInBackground(cardDetails: CardDetails): CardEntryActivityCommand {
        return try {
            // Assuming square will handle the background task
            val response = runBlocking(ioDispatcher) {
                repository.confirmPayment(Payment(cardDetails.nonce, Money(155.45f), Money(45f)))
            }

            if (response.isSuccessful) {
                CardEntryActivityCommand.Finish()
            } else {
                response as TaskResult.Error
                CardEntryActivityCommand.ShowError(response.errorMessage.orEmpty())
            }
        } catch (exception: IOException) {
            CardEntryActivityCommand.ShowError(context.getString(R.string.network_failure))
        }
    }
}