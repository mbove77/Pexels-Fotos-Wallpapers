package com.bove.martin.pexel.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.FileOperations
import com.bove.martin.pexel.domain.WallpaperOperations
import com.bove.martin.pexel.utils.UriToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Martín Bove on 07-Feb-20.
 * E-mail: mbove77@gmail.com
 */
class FullFotoActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val uriToBitmap = UriToBitmap()
    private val filesOperations = FileOperations()
    private val wallpaper = WallpaperOperations()

    val haveStoragePermission = MutableLiveData<Boolean>()
    val operationResult = MutableLiveData<OperationResult>()
    val savedFoto = MutableLiveData<Uri>()

    fun setStoragePermission(value: Boolean) {
        haveStoragePermission.postValue(value)
    }

    fun setWallpaper(url: String, isLockScreen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = uriToBitmap.getBitmap(url, getApplication<Application>().applicationContext)
            val resultOperation =  wallpaper.setWallpaper(bitmap, isLockScreen, getApplication<Application>().applicationContext)

            withContext(Dispatchers.Main) {
                operationResult.postValue(resultOperation)
            }
        }
    }

    fun downloadFoto(fotoUrl:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = uriToBitmap.getBitmap(fotoUrl, getApplication<Application>().applicationContext)
            val resultOperation = filesOperations.saveImage(getApplication<Application>().applicationContext, bitmap)

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