package com.bove.martin.pexel.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult


/**
 * Created by Martín Bove on 22-Sep-20.
 * E-mail: mbove77@gmail.com
 */

class UriToBitmap {

     suspend fun getBitmap(uri:String, context: Context): Bitmap? {

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
                .data(uri)
                .allowHardware(false)
                .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

}