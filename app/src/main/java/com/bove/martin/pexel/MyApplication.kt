package com.bove.martin.pexel

import android.app.Application
import com.bove.martin.pexel.di.RetrofitModule.retrofitModule
import com.bove.martin.pexel.di.ViewModelModule.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Martín Bove on 27-Oct-20.
 * E-mail: mbove77@gmail.com
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(listOf(retrofitModule, viewModelModule))
        }

    }
}