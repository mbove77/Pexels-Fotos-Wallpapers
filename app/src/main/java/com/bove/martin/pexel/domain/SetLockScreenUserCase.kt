package com.bove.martin.pexel.domain

import android.content.Context
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.WallpaperOperations
import com.bove.martin.pexel.domain.utils.UriToBitmap
import com.bove.martin.pexel.presentation.utils.UiText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 26/6/2022.
 * E-mail: mbove77@gmail.com
 */
class SetLockScreenUserCase @Inject constructor(private val wallpaperOperations: WallpaperOperations, @ApplicationContext val context: Context) {

    operator fun invoke(url: String): OperationResult {
        return try {
            val bitmap = UriToBitmap().getBitmap(url, context)
            if (bitmap != null)
                wallpaperOperations.setLockScreen(bitmap)
            else
                OperationResult(false, UiText.StringResource(R.string.set_lockscreen_error), null)
        } catch (e: Exception) {
            OperationResult(false, UiText.StringResource(R.string.load_image_error), null)
        }
    }
}