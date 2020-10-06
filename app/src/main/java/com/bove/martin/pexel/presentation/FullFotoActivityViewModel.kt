package com.bove.martin.pexel.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.data.model.OperationResult
import com.bove.martin.pexel.domain.WallpaperOperations
import com.bove.martin.pexel.domain.FileOperations

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
    private val context = getApplication<Application>().applicationContext

    private val _haveStoragePermission = MutableLiveData<Boolean>()
    val haveStoragePermission: LiveData<Boolean> get() = _haveStoragePermission

    fun setStoragePermission(value: Boolean) {
        _haveStoragePermission.value = value
    }

    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> get() = _operationResult

    private val _savedFoto = MutableLiveData<Uri>()
    val savedFoto: LiveData<Uri> get() = _savedFoto

    fun setWallpaper(url: String, isLockScreen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = uriToBitmap.getBitmap(url, context)
            val resultOperation =  wallpaper.setWallpaper(bitmap, isLockScreen, context)

            withContext(Dispatchers.Main) {
                _operationResult.value = resultOperation
            }
        }
    }

    fun downloadFoto(fotoUrl:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = uriToBitmap.getBitmap(fotoUrl, context)
            val resultOperation = filesOperations.saveImage(context, bitmap)

            withContext(Dispatchers.Main) {
               if (resultOperation.operationResult ) {
                   _savedFoto.value = resultOperation.resultObject as Uri
               } else {
                   _operationResult.value = resultOperation
               }
            }
        }
    }

    
}