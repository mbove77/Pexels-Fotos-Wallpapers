package com.bove.martin.pexel.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.R
import com.bove.martin.pexel.data.model.OperationResult
import com.bove.martin.pexel.domain.Wallpaper
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
    private val wallpaper = Wallpaper()
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
            
           if (bitmap != null) {
                try {
                    wallpaper.setWallpaper(bitmap, isLockScreen, context)
                    withContext(Dispatchers.Main) {
                        _operationResult.value = OperationResult(true, context.resources.getString(R.string.wallpaperChange))
                    }
                } catch (e: java.io.IOException) {
                    withContext(Dispatchers.Main) {
                        _operationResult.value = OperationResult(false, context.resources.getString(R.string.loadImageError))
                    }
                }
           } else {
               withContext(Dispatchers.Main) {
                   _operationResult.value = OperationResult(false, context.resources.getString(R.string.loadImageError))
               }
           }
        }

    }

    fun downloadFoto(fotoUrl:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = uriToBitmap.getBitmap(fotoUrl, context)

            if (bitmap != null) {
                try {
                    val imageUri = filesOperations.saveImage(context, bitmap)
                    if (imageUri != null) {
                        withContext(Dispatchers.Main) {
                            _savedFoto.value = imageUri
                        }
                    }  else {
                        withContext(Dispatchers.Main) {
                            _operationResult.value = OperationResult(false, context.resources.getString(R.string.loadImageError))
                        }
                    }
                } catch (e: java.io.IOException) {
                    withContext(Dispatchers.Main) {
                        _operationResult.value = OperationResult(false, context.resources.getString(R.string.loadImageError))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _operationResult.value = OperationResult(false, context.resources.getString(R.string.loadImageError))
                }
            }
        }

    }


    
}