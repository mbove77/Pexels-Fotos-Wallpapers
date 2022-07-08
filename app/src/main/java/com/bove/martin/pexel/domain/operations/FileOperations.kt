package com.bove.martin.pexel.domain.operations

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bove.martin.pexel.AppConstants.FILE_PROVIDER
import com.bove.martin.pexel.AppConstants.IMAGES_FOLDER_NAME
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.presentation.utils.UiText
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

/**
 * Created by Martín Bove on 24-Sep-20.
 * E-mail: mbove77@gmail.com
 */
class FileOperations @Inject constructor() {

    fun saveImage(context: Context, bitmap: Bitmap): OperationResult {
        val uniqueName = UUID.randomUUID().toString()

        val imageFileInformation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                getFileInformationForNewDevices(context, uniqueName)
             else
                getFileInformationForOldDevices(context, uniqueName)
        try {
            if (imageFileInformation.imageUri != null && imageFileInformation.outputString != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageFileInformation.outputString)
                imageFileInformation.outputString.close()
            } else {
                OperationResult(false,
                    UiText.StringResource(R.string.load_image_error), null)
            }
        } catch (error: RuntimeException) {
            Logger.e(error.toString(), error.localizedMessage)
            imageFileInformation.imageUri?.let { deleteFailedPhoto(imageFileInformation.imageUri, context) }

            return OperationResult(false,
                UiText.StringResource(R.string.load_image_error), null)
        }
        return OperationResult(true,
            UiText.StringResource(R.string.file_downloaded), imageFileInformation.imageUri.toString())
    }


    private fun getFileInformationForNewDevices(context: Context, uniqueName: String): ImageFileInformation {
        var outputString: OutputStream? = null
        return try {
            val contentValues = ContentValues()
            val resolver = context.contentResolver
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$uniqueName.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/$IMAGES_FOLDER_NAME")
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            outputString = imageUri?.let { resolver.openOutputStream(it) }

            ImageFileInformation(imageUri, outputString)
        } catch (error: RuntimeException) {
            Logger.e(error.toString(), error.localizedMessage)
            outputString?.close()
            ImageFileInformation(null, null)
        }
    }

    private fun getFileInformationForOldDevices(context: Context, uniqueName: String): ImageFileInformation {
        var outputString: OutputStream? = null
        return try {
            val imagesDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .toString() + File.separator + IMAGES_FOLDER_NAME
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val image = File(imagesDir, "$uniqueName.jpg")
            outputString = FileOutputStream(image)
            val imageUri = FileProvider.getUriForFile(context, FILE_PROVIDER, image)

             ImageFileInformation(imageUri, outputString)
        } catch (error: RuntimeException) {
            Logger.e(error.toString(), error.localizedMessage)
            outputString?.close()
            ImageFileInformation(null, null)
        }
    }

    private fun deleteFailedPhoto(imageUri: Uri, context: Context) {
        try {
            context.contentResolver.delete(imageUri,null,null)
        } catch (error: RuntimeException) {
            Logger.e(error.toString(), error.localizedMessage)
        }
    }
}

data class ImageFileInformation(val imageUri: Uri?, val outputString: OutputStream?)