package com.bove.martin.pexel.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.di.DispatchersModule.IoDispatcher
import com.bove.martin.pexel.di.DispatchersModule.MainDispatcher
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.usecases.DownloadFotoUseCase
import com.bove.martin.pexel.domain.usecases.SetLockScreenUserCase
import com.bove.martin.pexel.domain.usecases.SetWallpaperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 07-Feb-20.
 * E-mail: mbove77@gmail.com
 */
@HiltViewModel
class FullFotoActivityViewModel @Inject constructor(
    private val setWallpaperUseCase: SetWallpaperUseCase,
    private val setLockScreenUserCase: SetLockScreenUserCase,
    private val downloadFotoUseCase: DownloadFotoUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): ViewModel() {

    val haveStoragePermission = MutableLiveData<Boolean>()
    val operationResult = MutableLiveData<OperationResult>()
    val savedFoto = MutableLiveData<String>()

    fun setStoragePermission(value: Boolean) {
        haveStoragePermission.postValue(value)
    }

    fun setWallpaper(url: String) {
        viewModelScope.launch(ioDispatcher) {
            val resultOperation = setWallpaperUseCase(url)

            withContext(mainDispatcher) {
                operationResult.postValue(resultOperation)
            }
        }
    }

    fun setLockScreen(url: String) {
        viewModelScope.launch(ioDispatcher) {
            val resultOperation = setLockScreenUserCase(url)

            withContext(mainDispatcher) {
                operationResult.postValue(resultOperation)
            }
        }
    }

    fun downloadFoto(fotoUrl:String) {
        viewModelScope.launch(ioDispatcher) {
            val resultOperation = downloadFotoUseCase(fotoUrl)

            withContext(mainDispatcher) {
               if (resultOperation.operationResult ) {
                   savedFoto.postValue(resultOperation.resultObject as String)
               } else {
                   operationResult.postValue(resultOperation)
               }
            }
        }
    }

}
