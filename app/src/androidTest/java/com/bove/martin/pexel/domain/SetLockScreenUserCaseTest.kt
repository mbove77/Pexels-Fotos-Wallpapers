package com.bove.martin.pexel.domain

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bove.martin.pexel.AppConstants
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.WallpaperOperations
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Created by Martín Bove on 26/6/2022.
 * E-mail: mbove77@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class SetLockScreenUserCaseTest {
    private lateinit var appContext: Context
    private var resultUri: Uri? = null

    private lateinit var wallpaperOperations: WallpaperOperations

    @RelaxedMockK
    private lateinit var wallpaperOperationsMock: WallpaperOperations

    private lateinit var setLockScreenUserCase: SetLockScreenUserCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        wallpaperOperations = WallpaperOperations(appContext)
        setLockScreenUserCase = SetLockScreenUserCase(wallpaperOperations, appContext)
    }

    @Test
    fun when_call_with_setLockScreen_and_returns_operation_ok() {
        //Given
        val fotoUrl = "https://images.pexels.com/photos/8717/food-pot-kitchen-cooking.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=400&w=520"

        //When
        val result = setLockScreenUserCase(fotoUrl)

        //Then
        Assert.assertTrue(result.operationResult)
        Assert.assertTrue(result.resultMensaje == AppConstants.AppMessages.WALLPAPER_CHANGE.getMessage())
    }

    @Test
    fun when_call_with_setLockScreen_with_bad_fotoUrl_returns_operation_fail() {
        //Given
        val fotoUrl = "error image path"

        //When
        val result = setLockScreenUserCase(fotoUrl)

        //Then
        Assert.assertFalse(result.operationResult)
        Assert.assertTrue(result.resultMensaje == AppConstants.AppErrors.LOAD_IMAGE_ERROR.getErrorMessage())
    }


    @Test
    fun when_call_setLockScreen_and_returns_operation_fail() {
        //Given
        setLockScreenUserCase = SetLockScreenUserCase(wallpaperOperationsMock, appContext)
        val fotoUrl = "https://images.pexels.com/photos/8717/food-pot-kitchen-cooking.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=400&w=520"
        every { wallpaperOperationsMock.setWallpaper(any()) } returns OperationResult(false, AppConstants.AppErrors.LOAD_IMAGE_ERROR.getErrorMessage(), null)

        //When
        val result = setLockScreenUserCase(fotoUrl)

        //Then
        Assert.assertFalse(result.operationResult)
    }


    @After
    fun after() {
        if (resultUri != null) {
            val imagefile = File(resultUri.toString())
            imagefile.let { imagefile.delete() }
        }
    }
}