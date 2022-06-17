package com.bove.martin.pexel.presentation

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.domain.DownloadFotoUseCase
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
    private val downloadFotoUseCase: DownloadFotoUseCase
): ViewModel() {

    val haveStoragePermission = MutableLiveData<Boolean>()
    val operationResult = MutableLiveData<OperationResult>()
    val savedFoto = MutableLiveData<Uri>()

    fun setStoragePermission(value: Boolean) {
        haveStoragePermission.postValue(value)
    }

    fun setWallpaper(url: String, isLockScreen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultOperation = setWallpaperUseCase(url, isLockScreen)

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
                   savedFoto.postValue(resultOperation.resultObject as Uri)
               } else {
                   operationResult.postValue(resultOperation)
               }
            }
        }
    }
}