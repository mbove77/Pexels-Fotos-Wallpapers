package com.bove.martin.pexel.domain

import android.content.Context
import com.bove.martin.pexel.AppConstants
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.FileOperations
import com.bove.martin.pexel.domain.utils.UriToBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 17/6/2022.
 * E-mail: mbove77@gmail.com
 */
class DownloadFotoUseCase @Inject constructor(private  val fileOperations: FileOperations,  @ApplicationContext val context: Context) {

    operator fun invoke(fotoUrl:String): OperationResult {
        return try {
            val bitmap = UriToBitmap().getBitmap(fotoUrl, context)
            if (bitmap != null)
                fileOperations.saveImage(context, bitmap)
            else
                OperationResult(false, AppConstants.AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)
        }catch (e: Exception) {
            OperationResult(false, AppConstants.AppErrors.PHOTO_URL_ERROR.getErrorMessage(), null)
        }

    }
}