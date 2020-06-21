package com.xently.payment.di

import com.xently.payment.SquareCardEntryBackgroundHandler
import com.xently.payment.data.repository.square.ISquareRepository
import com.xently.payment.data.repository.square.SquareRepository
import com.xently.payment.data.repository.stripe.IStripeRepository
import com.xently.payment.data.repository.stripe.StripeRepository
import com.xently.payment.data.source.ISquareDataSource
import com.xently.payment.data.source.IStripeDataSource
import com.xently.payment.data.source.local.SquareLocalDataSource
import com.xently.payment.data.source.local.StripeLocalDataSource
import com.xently.payment.data.source.remote.SquareRemoteDataSource
import com.xently.payment.data.source.remote.StripeRemoteDataSource
import com.xently.payment.di.AppModule.LocalSquareDataSource
import com.xently.payment.di.AppModule.LocalStripeDataSource
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
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class SquareModule {
    @Binds
    abstract fun provideCardNonceBackgroundHandler(handler: SquareCardEntryBackgroundHandler): CardNonceBackgroundHandler
}