package com.bove.martin.pexel.domain

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.bove.martin.pexel.R
import com.bove.martin.pexel.data.model.OperationResult
import java.io.IOException

/**
 * Created by Martín Bove on 24-Sep-20.
 * E-mail: mbove77@gmail.com
 */
class WallpaperOperations {


    fun setWallpaper(resource: Bitmap?, isLockScreen: Boolean, context: Context): OperationResult {
        val wallpaperManager = WallpaperManager.getInstance(context)

        if (resource == null) {
            return OperationResult(false, context.getString(R.string.loadImageError), null)
        }
        try {
            if (isLockScreen) {
                if (Build.VERSION.SDK_INT >= 24) {
                    wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
                } else {
                    return OperationResult(false, context.getString(R.string.wallpaperLockError), null)
                }
            } else {
                wallpaperManager.setBitmap(resource)
            }
        } catch (e: IOException) {
            return OperationResult(false, context.getString(R.string.loadImageError), null)
        }
        return OperationResult(true, context.getString(R.string.wallpaperChange), null)
    }
}