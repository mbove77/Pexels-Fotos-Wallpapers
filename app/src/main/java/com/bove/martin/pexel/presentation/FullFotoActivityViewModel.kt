package com.bove.martin.pexel.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.domain.DownloadFotoUseCase
import com.bove.martin.pexel.domain.SetLockScreenUserCase
import com.bove.martin.pexel.domain.SetWallpaperUseCase
import com.bove.martin.pexel.domain.model.OperationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val downloadFotoUseCase: DownloadFotoUseCase
): ViewModel() {

    val haveStoragePermission = MutableLiveData<Boolean>()
    val operationResult = MutableLiveData<OperationResult>()
    val savedFoto = MutableLiveData<String>()

    fun setStoragePermission(value: Boolean) {
        haveStoragePermission.postValue(value)
    }

    fun setWallpaper(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultOperation = setWallpaperUseCase(url)

            withContext(Dispatchers.Main) {
                operationResult.postValue(resultOperation)
            }
        }
    }

    fun setLockScreen(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultOperation = setLockScreenUserCase(url)

            withContext(Dispatchers.Main) {
                operationResult.postValue(resultOperation)
            }
        }
    }

    fun downloadFoto(fotoUrl:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultOperation = downloadFotoUseCase(fotoUrl)

            withContext(Dispatchers.Main) {
               if (resultOperation.operationResult ) {
                   savedFoto.postValue(resultOperation.resultObject as String)
               } else {
                   operationResult.postValue(resultOperation)
               }
            }
        }
    }

}