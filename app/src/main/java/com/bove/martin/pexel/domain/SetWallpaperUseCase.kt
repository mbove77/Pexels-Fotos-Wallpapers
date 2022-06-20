package com.bove.martin.pexel.domain

import android.content.Context
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.WallpaperOperations
import com.bove.martin.pexel.domain.utils.UriToBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 17/6/2022.
 * E-mail: mbove77@gmail.com
 */
class SetWallpaperUseCase  @Inject constructor(private val wallpaperOperations: WallpaperOperations, @ApplicationContext val context: Context) {
    operator fun invoke(url: String, isLockScreen: Boolean): OperationResult  {
        val bitmap = UriToBitmap().getBitmap(url, context)
        return wallpaperOperations.setWallpaper(bitmap, isLockScreen, context)
    }
}