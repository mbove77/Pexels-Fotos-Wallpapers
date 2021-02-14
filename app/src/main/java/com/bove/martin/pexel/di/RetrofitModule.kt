package com.bove.martin.pexel.di

import com.bove.martin.pexel.data.repositories.FotosRepository
import com.bove.martin.pexel.data.repositories.PopularSearchesRepository
import com.bove.martin.pexel.data.retrofit.PexelService
import com.bove.martin.pexel.data.retrofit.RetrofitService
import org.koin.dsl.module

/**
 * Created by Martín Bove on 13-Sep-20.
 * E-mail: mbove77@gmail.com
 */
object RetrofitModule {

    val retrofitModule = module {
        single {
            retrofit()
        }
        single {
            FotosRepository(get())
        }
        single {
            PopularSearchesRepository()
        }
    }

    private fun retrofit(): PexelService {
        return RetrofitService().createService(PexelService::class.java)
    }
}
