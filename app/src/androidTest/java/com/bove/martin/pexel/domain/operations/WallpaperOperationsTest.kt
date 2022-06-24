package com.bove.martin.pexel.domain.operations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bove.martin.pexel.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Martín Bove on 06-Oct-20.
 * E-mail: mbove77@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class WallpaperOperationsTest {

    private lateinit var appContext: Context
    private lateinit var testBmp: Bitmap

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        testBmp= BitmapFactory.decodeResource(appContext.resources, R.drawable.pexels)
    }

    @Test
    fun test_set_wallpaper_bitmap_null_return_false() {
        //Given
        val wallpaper = WallpaperOperations()

        //When
        val result= wallpaper.setWallpaper(null,false, appContext)

        //Then
        assertNotNull(result)
        assertFalse(result.operationResult)
    }

    @Test
    fun test_set_wallpaper_bitmap_return_true() {
        //Given
        val wallpaper = WallpaperOperations()

        //When
        val result= wallpaper.setWallpaper(testBmp,false, appContext)

        //Then
        assertNotNull(result)
        assertTrue(result.operationResult)
    }

    @Test
    fun test_set_lock_bitmap_api21_return_false() {
        //Given
        val wallpaper = WallpaperOperations()

        //When
        val result= wallpaper.setWallpaper(testBmp,true, appContext)

        //Then
        assertNotNull(result)
        if(Build.VERSION.SDK_INT >= 24) {
             assertTrue(result.operationResult)
        } else {
            assertFalse(result.operationResult)
        }
    }

}