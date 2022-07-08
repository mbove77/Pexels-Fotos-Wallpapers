package com.bove.martin.pexel.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bove.martin.pexel.domain.DownloadFotoUseCase
import com.bove.martin.pexel.domain.SetLockScreenUserCase
import com.bove.martin.pexel.domain.SetWallpaperUseCase
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.presentation.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

/**
 * Created by Martín Bove on 24/6/2022.
 * E-mail: mbove77@gmail.com
 */
@ExperimentalCoroutinesApi
class FullFotoActivityViewModelTest {

    private lateinit var fullFotoActivityViewModel: FullFotoActivityViewModel

    @RelaxedMockK
    private lateinit var setWallpaperUseCase: SetWallpaperUseCase
    @RelaxedMockK
    private lateinit var downloadFotoUseCase: DownloadFotoUseCase
    @RelaxedMockK
    private lateinit var setLockScreenUserCase: SetLockScreenUserCase

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fullFotoActivityViewModel = FullFotoActivityViewModel(
            setWallpaperUseCase,
            setLockScreenUserCase,
            downloadFotoUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @Test
    fun `when call setWallpaper with valid params return ok result`() = runTest {
        //Given
        coEvery { setWallpaperUseCase(any()) } returns OperationResult(true, UiText.DynamicString("Test"), null)

        //When
        fullFotoActivityViewModel.setWallpaper("fileURL")

        //Then
        coVerify(exactly = 1) { setWallpaperUseCase(any()) }
        Assert.assertTrue(fullFotoActivityViewModel.operationResult.value!!.operationResult)
    }

    @Test
    fun `when call setWallpaper and return fail result`() = runTest {
        //Given
        coEvery { setWallpaperUseCase(any()) } returns OperationResult(false, UiText.DynamicString("Test"), null)

        //When
        fullFotoActivityViewModel.setWallpaper("fileURL")

        //Then
        coVerify(exactly = 1) { setWallpaperUseCase(any()) }
        Assert.assertFalse(fullFotoActivityViewModel.operationResult.value!!.operationResult)
    }


    @Test
    fun `when call downloadFoto with valid params set savedFoto liveData`() = runTest {
        //Given
        coEvery { downloadFotoUseCase(any()) } returns OperationResult(true, UiText.DynamicString("Test"), "string")

        //When
        fullFotoActivityViewModel.downloadFoto("fileURL")

        //Then
        coVerify(exactly = 1) { downloadFotoUseCase("fileURL") }
        Assert.assertNotNull(fullFotoActivityViewModel.savedFoto.value)
    }

    @Test
    fun `when call downloadFoto and return fail result`() = runTest {
        //Given
        coEvery { downloadFotoUseCase("fileURL") } returns OperationResult(false, UiText.DynamicString("Test"), null)

        //When
        fullFotoActivityViewModel.downloadFoto("fileURL")

        //Then
        coVerify(exactly = 1) { downloadFotoUseCase(any()) }
        Assert.assertFalse(fullFotoActivityViewModel.operationResult.value!!.operationResult)
    }

    @Test
    fun `when call setStoragePermission set liveData`() = runTest {
        //Given
        val storagePermission = true

        //When
        fullFotoActivityViewModel.setStoragePermission(storagePermission)

        //Then
        Assert.assertTrue(fullFotoActivityViewModel.haveStoragePermission.value!!)
    }


    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }
}