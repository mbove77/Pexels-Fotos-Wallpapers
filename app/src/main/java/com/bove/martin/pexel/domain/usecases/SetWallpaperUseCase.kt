package com.bove.martin.pexel.domain.usecases

import android.content.Context
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.WallpaperOperations
import com.bove.martin.pexel.domain.utils.UriToBitmap
import com.bove.martin.pexel.presentation.utils.UiText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 17/6/2022.
 * E-mail: mbove77@gmail.com
 */

open class SetWallpaperUseCase  @Inject constructor(
    private val wallpaperOperations: WallpaperOperations,
    @ApplicationContext val context: Context) {

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    open operator fun invoke(url: String): OperationResult  {
        return try {
            val bitmap = UriToBitmap().getBitmap(url, context)
            if (bitmap != null) {
                wallpaperOperations.setWallpaper(bitmap)
            } else
                OperationResult(false,
                    UiText.StringResource(R.string.set_wallpaper_error), null)
        } catch (e: Exception) {
            OperationResult(false,
                UiText.StringResource(R.string.photo_url_error), null)
        }
    }
}
