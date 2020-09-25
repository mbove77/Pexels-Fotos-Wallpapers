package com.bove.martin.pexel.domain

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.bove.martin.pexel.R
import java.io.IOException

/**
 * Created by Martín Bove on 24-Sep-20.
 * E-mail: mbove77@gmail.com
 */
class Wallpaper {

    @Throws(IOException::class)
    fun setWallpaper(resource: Bitmap, isLockScreen: Boolean, context: Context) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        if (isLockScreen) {
            if (Build.VERSION.SDK_INT >= 24) {
                wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
            } else {
                throw IOException(context.resources.getString(R.string.wallpaperLockError))
            }
        } else {
            wallpaperManager.setBitmap(resource)
        }
    }
}