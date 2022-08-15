package com.bove.martin.pexel.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Created by Martín Bove on 13/7/2022.
 * E-mail: mbove77@gmail.com
 */
object DispatchersModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object DispatcherModule {
        @IoDispatcher
        @Provides
        fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @MainDispatcher
        @Provides
        fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IoDispatcher

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MainDispatcher
}