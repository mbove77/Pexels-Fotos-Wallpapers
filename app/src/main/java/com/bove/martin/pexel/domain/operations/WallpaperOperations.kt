package com.bove.martin.pexel.domain.operations

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.bove.martin.pexel.AppConstants.AppErrors
import com.bove.martin.pexel.AppConstants.AppMessages
import com.bove.martin.pexel.domain.model.OperationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Martín Bove on 24-Sep-20.
 * E-mail: mbove77@gmail.com
 */
class WallpaperOperations @Inject constructor(@ApplicationContext val context: Context) {
    private val wallpaperManager = WallpaperManager.getInstance(context)

    fun setWallpaper(resource: Bitmap): OperationResult {
        try {
            wallpaperManager.setBitmap(resource)
        } catch (e: IOException) {
            return OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)
        }
        return OperationResult(true, AppMessages.WALLPAPER_CHANGE.getMessage(), null)
    }

    fun setLockScreen(resource: Bitmap): OperationResult {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
            } else {
                return OperationResult(false, AppErrors.LOCKSCREEN_API_VERSION_ERROR.getErrorMessage(), null)
            }
        } catch (e: IOException) {
            return OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)
        }
        return OperationResult(true, AppMessages.WALLPAPER_CHANGE.getMessage(), null)
    }
}