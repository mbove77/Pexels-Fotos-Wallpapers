package com.bove.martin.pexel.domain

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.operations.FileOperations
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
        testBmp= BitmapFactory.decodeResource(appContext.resources, R.drawable.pexels)
    }

    @Test
    fun operation_with_null_bitmap_return_false() {
        val operation = FileOperations()
        val result = operation.saveImage(appContext, null)

        Assert.assertNotNull(result)
        Assert.assertFalse(result.operationResult)
    }

    @Test
    fun operation_with_bitmap_return_true() {
        val operation = FileOperations()

        val result = operation.saveImage(appContext, testBmp)

        Assert.assertNotNull(result)
        Assert.assertTrue(result.operationResult)
        resultUri = result.resultObject as Uri
    }

    @Test
    @TargetApi(19)
    fun operation_with_bitmap_api19_return_true() {
        val operation = FileOperations()

        val result = operation.saveImage(appContext, testBmp)

        Assert.assertNotNull(result)
        Assert.assertTrue(result.operationResult)
        resultUri = result.resultObject as Uri
    }

    @After
    fun after() {
        val imagefile = File(resultUri.toString())
        imagefile.let { imagefile.delete() }
    }

}