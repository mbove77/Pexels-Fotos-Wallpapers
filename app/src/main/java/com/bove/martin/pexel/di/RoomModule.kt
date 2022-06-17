package com.bove.martin.pexel.di

import android.content.Context
import androidx.room.Room
import com.bove.martin.pexel.data.database.SearchesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Martín Bove on 16/6/2022.
 * E-mail: mbove77@gmail.com
 */

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private val DATABASE_NAME = "searches_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, SearchesDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideSearchesDato(db: SearchesDatabase) = db.getSearchesDato()
}