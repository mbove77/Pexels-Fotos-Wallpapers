package com.bove.martin.pexel.domain.operations

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.presentation.utils.UiText
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
            return OperationResult(false, UiText.StringResource(R.string.load_image_error), null)
        }
        return OperationResult(true, UiText.StringResource(R.string.wallpaper_change), null)
    }

    fun setLockScreen(resource: Bitmap): OperationResult {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
            } else {
                return OperationResult(false, UiText.StringResource(R.string.lockscreen_api_version_error), null)
            }
        } catch (e: IOException) {
            return OperationResult(false, UiText.StringResource(R.string.load_image_error), null)
        }
        return OperationResult(true, UiText.StringResource(R.string.wallpaper_change), null)
    }
}