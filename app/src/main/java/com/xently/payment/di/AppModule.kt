package com.xently.payment.di

import com.xently.payment.SquareCardEntryBackgroundHandler
import com.xently.payment.data.repository.braintree.BraintreeRepository
import com.xently.payment.data.repository.braintree.IBraintreeRepository
import com.xently.payment.data.repository.mpesa.IMpesaRepository
import com.xently.payment.data.repository.mpesa.MpesaRepository
import com.xently.payment.data.repository.square.ISquareRepository
import com.xently.payment.data.repository.square.SquareRepository
import com.xently.payment.data.repository.stripe.IStripeRepository
import com.xently.payment.data.repository.stripe.StripeRepository
import com.xently.payment.data.source.IBraintreeDataSource
import com.xently.payment.data.source.IMpesaDataSource
import com.xently.payment.data.source.ISquareDataSource
import com.xently.payment.data.source.IStripeDataSource
import com.xently.payment.data.source.local.BraintreeLocalDataSource
import com.xently.payment.data.source.local.MpesaLocalDataSource
import com.xently.payment.data.source.local.SquareLocalDataSource
import com.xently.payment.data.source.local.StripeLocalDataSource
import com.xently.payment.data.source.remote.BraintreeRemoteDataSource
import com.xently.payment.data.source.remote.MpesaRemoteDataSource
import com.xently.payment.data.source.remote.SquareRemoteDataSource
import com.xently.payment.data.source.remote.StripeRemoteDataSource
import com.xently.payment.di.AppModule.LocalBraintreeDataSource
import com.xently.payment.di.AppModule.LocalSquareDataSource
import com.xently.payment.di.AppModule.LocalStripeDataSource
import com.xently.payment.di.AppModule.RemoteBraintreeDataSource
import com.xently.payment.di.AppModule.RemoteSquareDataSource
import com.xently.payment.di.AppModule.RemoteStripeDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import sqip.CardNonceBackgroundHandler
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteStripeDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalStripeDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteSquareDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalSquareDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteBraintreeDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalBraintreeDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteMpesaDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalMpesaDataSource

    @Provides
    @RemoteStripeDataSource
    @Singleton
    fun provideRemoteStripeDataSource(): IStripeDataSource = StripeRemoteDataSource()

    @Provides
    @LocalStripeDataSource
    @Singleton
    fun provideLocalStripeDataSource(): IStripeDataSource = StripeLocalDataSource()

    @Provides
    @RemoteSquareDataSource
    @Singleton
    fun provideRemoteSquareDataSource(): ISquareDataSource = SquareRemoteDataSource()

    @Provides
    @LocalSquareDataSource
    @Singleton
    fun provideLocalSquareDataSource(): ISquareDataSource = SquareLocalDataSource()

    @Provides
    @RemoteBraintreeDataSource
    @Singleton
    fun provideRemoteBraintreeDataSource(): IBraintreeDataSource = BraintreeRemoteDataSource()

    @Provides
    @LocalBraintreeDataSource
    @Singleton
    fun provideLocalBraintreeDataSource(): IBraintreeDataSource = BraintreeLocalDataSource()

    @Provides
    @RemoteMpesaDataSource
    @Singleton
    fun provideRemoteMpesaDataSource(): IMpesaDataSource = MpesaRemoteDataSource()

    @Provides
    @LocalMpesaDataSource
    @Singleton
    fun provideLocalMpesaDataSource(): IMpesaDataSource = MpesaLocalDataSource()

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideStripeRepository(
        @LocalStripeDataSource local: IStripeDataSource,
        @RemoteStripeDataSource remote: IStripeDataSource,
        ioDispatcher: CoroutineDispatcher
    ): IStripeRepository = StripeRepository(local, remote, ioDispatcher)

    @Provides
    @Singleton
    fun provideSquareRepository(
        @LocalSquareDataSource local: ISquareDataSource,
        @RemoteSquareDataSource remote: ISquareDataSource,
        ioDispatcher: CoroutineDispatcher
    ): ISquareRepository = SquareRepository(local, remote, ioDispatcher)

    @Provides
    @Singleton
    fun provideBraintreeRepository(
        @LocalBraintreeDataSource local: IBraintreeDataSource,
        @RemoteBraintreeDataSource remote: IBraintreeDataSource,
        ioDispatcher: CoroutineDispatcher
    ): IBraintreeRepository = BraintreeRepository(local, remote, ioDispatcher)

    @Provides
    @Singleton
    fun providMpesaRepository(
        @AppModule.LocalMpesaDataSource local: IMpesaDataSource,
        @AppModule.RemoteMpesaDataSource remote: IMpesaDataSource,
        ioDispatcher: CoroutineDispatcher
    ): IMpesaRepository = MpesaRepository(local, remote, ioDispatcher)
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class SquareModule {
    @Binds
    abstract fun provideCardNonceBackgroundHandler(handler: SquareCardEntryBackgroundHandler): CardNonceBackgroundHandler
}