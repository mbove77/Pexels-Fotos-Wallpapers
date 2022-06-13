package com.bove.martin.pexel.di

import com.bove.martin.pexel.data.model.Foto
import com.bove.martin.pexel.data.retrofit.GsonDeserializador
import com.bove.martin.pexel.data.retrofit.PexelService
import com.bove.martin.pexel.utils.AppConstants
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Martín Bove on 13-Sep-20.
 * E-mail: mbove77@gmail.com
 */

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapter(
                        TypeToken.getParameterized(List::class.java, Foto::class.java).type,
                        GsonDeserializador()
                    ).create()
                )
            )
            .build()
    }

    @Singleton
    @Provides
    fun providePexelApi(retrofit: Retrofit): PexelService {
        return retrofit.create(PexelService::class.java)
    }
}
