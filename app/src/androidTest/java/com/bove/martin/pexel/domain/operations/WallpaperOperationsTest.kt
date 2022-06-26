package com.bove.martin.pexel.domain.operations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bove.martin.pexel.AppConstants.AppErrors
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.OperationResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
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
    private lateinit var wallpaperOperations: WallpaperOperations

    @RelaxedMockK
    private lateinit var wallpaperOperationsMock: WallpaperOperations
    private lateinit var appContext: Context
    private lateinit var testBmp: Bitmap

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        testBmp = BitmapFactory.decodeResource(appContext.resources, R.drawable.pexels)
        wallpaperOperations = WallpaperOperations(appContext)
    }

    @Test
    fun when_set_wallpaper_and_return_true() {
        //Given


        //When
        val result = wallpaperOperations.setWallpaper(testBmp)

        //Then
        assertNotNull(result)
        assertTrue(result.operationResult)
    }

    @Test
    fun when_set_wallpaper_and_return_false() {
        //Given
        every { wallpaperOperationsMock.setWallpaper(any()) } returns OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)

        //When
        val result= wallpaperOperationsMock.setWallpaper(testBmp)

        //Then
        assertNotNull(result)
        assertFalse(result.operationResult)
    }

    @Test
    fun when_set_lockScreen_and_return_false() {
        //Given
        every { wallpaperOperationsMock.setLockScreen(any()) } returns OperationResult(false, AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)

        //When
        val result = wallpaperOperationsMock.setLockScreen(testBmp)

        //Then
        assertNotNull(result)
        assertFalse(result.operationResult)
    }

    @Test
    fun when_set_lockScreen_and_return_true() {
        //Given


        //When
        val result = wallpaperOperations.setLockScreen(testBmp)

        //Then
        assertNotNull(result)
        assertTrue(result.operationResult)
    }

    @Test
    fun when_set_lockScreen_with_less_of_api24_return_false() {
        //Given

        //When
        val result = wallpaperOperations.setLockScreen(testBmp)

        //Then
        assertNotNull(result)
        if(Build.VERSION.SDK_INT >= 24) {
             assertTrue(result.operationResult)
        } else {
            assertFalse(result.operationResult)
        }
    }

}