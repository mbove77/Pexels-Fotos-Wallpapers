package com.bove.martin.pexel.di

import com.bove.martin.pexel.presentation.MainActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Martín Bove on 27-Oct-20.
 * E-mail: mbove77@gmail.com
 */
object ViewModelModule {
    val viewModelModule = module {

        viewModel {
            MainActivityViewModel(get(), get())
        }
    }
}