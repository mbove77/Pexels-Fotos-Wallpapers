package com.bove.martin.pexel.domain.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide


/**
 * Created by Martín Bove on 22-Sep-20.
 * E-mail: mbove77@gmail.com
 */

class UriToBitmap {

     fun getBitmap(uri:String, context: Context): Bitmap? {
         return Glide.with(context).asBitmap().load(uri).submit().get()
    }

}