package com.bove.martin.pexel.domain

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bove.martin.pexel.utils.AppConstants.DEFAULT_FILE_NAME
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import com.bove.martin.pexel.utils.AppConstants.IMAGES_FOLDER_NAME

/**
 * Created by Martín Bove on 24-Sep-20.
 * E-mail: mbove77@gmail.com
 */
class FileOperations {

    @Throws(IOException::class)
    fun saveImage(context: Context, bitmap: Bitmap): Uri? {
        val fos: OutputStream?
        val imageUri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, DEFAULT_FILE_NAME)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/$IMAGES_FOLDER_NAME")
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = resolver.openOutputStream(imageUri!!)
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val image = File(imagesDir, "$DEFAULT_FILE_NAME.jpg")
            fos = FileOutputStream(image)
            imageUri = FileProvider.getUriForFile(context, "com.bove.martin.pexel.provider", image)
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos!!.flush()
        fos.close()
        return imageUri
    }

}