package com.bove.martin.pexel.domain.usecases

import android.content.Context
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.FileOperations
import com.bove.martin.pexel.domain.utils.UriToBitmap
import com.bove.martin.pexel.presentation.utils.UiText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 17/6/2022.
 * E-mail: mbove77@gmail.com
 */
class DownloadFotoUseCase @Inject constructor(
    private  val fileOperations: FileOperations,
    @ApplicationContext val context: Context) {

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    operator fun invoke(fotoUrl:String): OperationResult {
        return try {
            val bitmap = UriToBitmap().getBitmap(fotoUrl, context)
            if (bitmap != null)
                fileOperations.saveImage(context, bitmap)
            else
                OperationResult(false,
                    UiText.StringResource(R.string.load_image_error), null)
        }catch (e: Exception) {
            OperationResult(false,
                UiText.StringResource(R.string.photo_url_error), null)
        }
    }
}
