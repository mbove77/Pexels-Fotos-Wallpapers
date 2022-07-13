package com.bove.martin.pexel.domain.operations

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bove.martin.pexel.R
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Created by Martín Bove on 06-Oct-20.
 * E-mail: mbove77@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class FileOperationsTest {
    private lateinit var appContext: Context
    private lateinit var testBmp: Bitmap
    private var resultUri: Uri? = null

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        testBmp = BitmapFactory.decodeResource(appContext.resources, R.drawable.pexels)
    }

    @Test
    fun operation_with_bitmap_return_true() {
        //Given
        val operation = FileOperations()

        //When
        val result = operation.saveImage(appContext, testBmp)

        //Then
        Assert.assertNotNull(result)
        Assert.assertTrue(result.operationResult)
        resultUri = Uri.parse(result.resultObject.toString())
    }

    @Test
    @TargetApi(19)
    fun operation_with_bitmap_api19_return_true() {
        //Given
        val operation = FileOperations()

        //When
        val result = operation.saveImage(appContext, testBmp)

        //Then
        Assert.assertNotNull(result)
        Assert.assertTrue(result.operationResult)
        resultUri = Uri.parse(result.resultObject.toString())
    }

    @Test
    @TargetApi(29)
    fun operation_with_bitmap_api29_return_true() {
        //Given
        val operation = FileOperations()

        //When
        val result = operation.saveImage(appContext, testBmp)

        //Then
        Assert.assertNotNull(result)
        Assert.assertTrue(result.operationResult)
        resultUri = Uri.parse(result.resultObject.toString())
    }

    @After
    fun after() {
        if (resultUri != null) {
            val imagefile = File(resultUri.toString())
            imagefile.let { imagefile.delete() }
        }
    }

}