package com.bove.martin.pexel.domain.operations

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bove.martin.pexel.AppConstants.IMAGES_FOLDER_NAME
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.OperationResult
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

/**
 * Created by Martín Bove on 24-Sep-20.
 * E-mail: mbove77@gmail.com
 */
class FileOperations @Inject constructor() {
    fun saveImage(context: Context, bitmap: Bitmap?): OperationResult {
        val fos: OutputStream?
        var imageUri: Uri? = null
        val resolver = context.contentResolver
        val uniqueName = UUID.randomUUID().toString()

        if (bitmap == null) {
            return OperationResult(false, context.getString(R.string.loadImageError), null)
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$uniqueName.jpg" )
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/$IMAGES_FOLDER_NAME")
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = resolver.openOutputStream(imageUri!!)
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME
                val file = File(imagesDir)
                if (!file.exists()) {
                    file.mkdir()
                }
                val image = File(imagesDir, "$uniqueName.jpg")
                fos = FileOutputStream(image)
                imageUri = FileProvider.getUriForFile(context, "com.bove.martin.pexel.fileProvider", image)
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos?.let { Objects.requireNonNull(fos).close() }
        } catch (e: IOException) {
            imageUri?.let { resolver.delete(it, null, null) }
            return OperationResult(false, context.getString(R.string.loadImageError), null)
        }
        return if (imageUri == null) {
            OperationResult(false, context.getString(R.string.loadImageError), null)
        } else {
            OperationResult(true, context.getString(R.string.fileDownload), imageUri)
        }
    }
}