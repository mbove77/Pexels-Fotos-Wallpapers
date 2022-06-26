package com.bove.martin.pexel.domain

import android.content.Context
import com.bove.martin.pexel.AppConstants.AppErrors
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.WallpaperOperations
import com.bove.martin.pexel.domain.utils.UriToBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 17/6/2022.
 * E-mail: mbove77@gmail.com
 */
open class SetWallpaperUseCase  @Inject constructor(private val wallpaperOperations: WallpaperOperations, @ApplicationContext val context: Context) {

    open operator fun invoke(url: String): OperationResult  {
        return try {
            val bitmap = UriToBitmap().getBitmap(url, context)
            if (bitmap != null) {
                wallpaperOperations.setWallpaper(bitmap)
            } else
                OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)
        } catch (e: Exception) {
            OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)
        }
    }
}