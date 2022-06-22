package com.bove.martin.pexel.domain.operations

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.bove.martin.pexel.AppConstants.AppErrors
import com.bove.martin.pexel.AppConstants.AppMessages
import com.bove.martin.pexel.domain.model.OperationResult
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Martín Bove on 24-Sep-20.
 * E-mail: mbove77@gmail.com
 */
class WallpaperOperations @Inject constructor() {

    fun setWallpaper(resource: Bitmap?, isLockScreen: Boolean, context: Context): OperationResult {
        val wallpaperManager = WallpaperManager.getInstance(context)

        if (resource == null) {
            return OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)
        }
        try {
            if (isLockScreen) {
                if (Build.VERSION.SDK_INT >= 24) {
                    wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
                } else {
                    return OperationResult(false, AppErrors.WALLPAPER_LOCK_ERROR.getErrorMessage(), null)
                }
            } else {
                wallpaperManager.setBitmap(resource)
            }
        } catch (e: IOException) {
            return OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)
        }
        return OperationResult(true, AppMessages.WALLPAPER_CHANGE.getMessage(), null)
    }
}