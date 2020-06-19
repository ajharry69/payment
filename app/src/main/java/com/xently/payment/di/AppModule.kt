package com.xently.payment.di

import com.xently.payment.data.repository.stripe.StripePaymentRepository
import com.xently.payment.data.repository.stripe.IStripePaymentRepository
import com.xently.payment.data.source.local.StripePaymentLocalDataSource
import com.xently.payment.data.source.remote.StripePaymentRemoteDataSource
import com.xently.payment.data.source.IStripePaymentDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteStripePaymentDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalStripePaymentDataSource

    @Provides
    @RemoteStripePaymentDataSource
    @Singleton
    fun provideRemoteStripePaymentDataSource(): IStripePaymentDataSource {
        return StripePaymentRemoteDataSource()
    }

    @Provides
    @LocalStripePaymentDataSource
    @Singleton
    fun provideLocalStripePaymentDataSource(): IStripePaymentDataSource {
        return StripePaymentLocalDataSource()
    }

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideStripePaymentRepository(
        @AppModule.LocalStripePaymentDataSource local: IStripePaymentDataSource,
        @AppModule.RemoteStripePaymentDataSource remote: IStripePaymentDataSource,
        ioDispatcher: CoroutineDispatcher
    ): IStripePaymentRepository = StripePaymentRepository(local, remote, ioDispatcher)
}