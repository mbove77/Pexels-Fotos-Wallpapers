package com.bove.martin.pexel.domain

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.operations.FileOperations
import com.bove.martin.pexel.domain.usecases.DownloadFotoUseCase
import com.bove.martin.pexel.presentation.utils.UiText
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
 * Created by Martín Bove on 24/6/2022.
 * E-mail: mbove77@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class DownloadFotoUseCaseTest {
    private lateinit var appContext: Context
    private var resultUri: Uri? = null
    private lateinit var fileOperations: FileOperations
    @RelaxedMockK
    private lateinit var fileOperationsMock: FileOperations
    private lateinit var downloadFotoUseCase: DownloadFotoUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        fileOperations = FileOperations()
        downloadFotoUseCase = DownloadFotoUseCase(fileOperations, appContext)
    }


    @Test
    fun when_called_return_file_uri_string() {
        //Given
        val fotoUrl = "https://images.pexels.com/photos/8717/food-pot-kitchen-cooking.jpg" +
                "?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=400&w=520"

        //When
        val result = downloadFotoUseCase(fotoUrl)

        //Then
        Assert.assertTrue(result.operationResult)
        Assert.assertTrue(result.resultObject is String)
    }

    @Test
    fun when_called_return_error() {
        //Given
        downloadFotoUseCase = DownloadFotoUseCase(fileOperationsMock, appContext)
        val fotoUrl = "https://images.pexels.com/photos/8717/food-pot-kitchen-cooking.jpg" +
                "?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=400&w=520"
        every { fileOperationsMock.saveImage(any(), any()) }returns
                OperationResult(false, UiText.DynamicString("Error"), null)

        //When
        val result = downloadFotoUseCase(fotoUrl)

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
