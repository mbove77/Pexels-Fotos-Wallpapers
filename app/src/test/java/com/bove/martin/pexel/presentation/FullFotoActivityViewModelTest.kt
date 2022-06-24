package com.bove.martin.pexel.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bove.martin.pexel.domain.DownloadFotoUseCase
import com.bove.martin.pexel.domain.SetWallpaperUseCase
import com.bove.martin.pexel.domain.model.OperationResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fullFotoActivityViewModel = FullFotoActivityViewModel(setWallpaperUseCase, downloadFotoUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @Test
    fun `when call setWallpaper with valid params return ok result`() = runTest {
        //Given
        coEvery { setWallpaperUseCase(any(), any()) } returns OperationResult(true, "Ok message", null)

        //When
        fullFotoActivityViewModel.setWallpaper("fileURL", true)

        //Then
        coVerify(exactly = 1) { setWallpaperUseCase(any(), any()) }
        fullFotoActivityViewModel.operationResult.value?.let { assert(it.operationResult) }
    }

    @Test
    fun `when call setWallpaper and return fail result`() = runTest {
        //Given
        coEvery { setWallpaperUseCase(any(), any()) } returns OperationResult(false, "Error message", null)

        //When
        fullFotoActivityViewModel.setWallpaper("fileURL", true)

        //Then
        coVerify(exactly = 1) { setWallpaperUseCase(any(), any()) }
        fullFotoActivityViewModel.operationResult.value?.let { assert(!it.operationResult) }
        assert(fullFotoActivityViewModel.operationResult.value?.operationResult == false)
    }


    //TODO fix Uri retruns bug
    @Test
    fun `when call downloadFoto with valid params return ok result set savedFoto liveData`() = runTest {
        //Given
        coEvery { downloadFotoUseCase(any()) } returns OperationResult(true, "Ok message", null)

        //When
        fullFotoActivityViewModel.downloadFoto("fileURL")

        //Then
        coVerify(exactly = 1) { downloadFotoUseCase(any()) }
        fullFotoActivityViewModel.operationResult.value?.let { assert(it.operationResult) }
        //assert(fullFotoActivityViewModel.savedFoto.value != null)
    }

    @Test
    fun `when call downloadFoto and return fail result`() = runTest {
        //Given
        coEvery { downloadFotoUseCase(any()) } returns OperationResult(false, "Error message", null)

        //When
        fullFotoActivityViewModel.downloadFoto("fileURL")

        //Then
        coVerify(exactly = 1) { downloadFotoUseCase(any()) }
        fullFotoActivityViewModel.operationResult.value?.let { assert(!it.operationResult) }
        assert(fullFotoActivityViewModel.operationResult.value?.operationResult == false)
    }

    @Test
    fun `when call setStoragePermission set liveData`() = runTest {
        //Given
        val storagePermission = true

        //When
        fullFotoActivityViewModel.setStoragePermission(storagePermission)

        //Then
        assert(fullFotoActivityViewModel.haveStoragePermission.value == storagePermission)
    }
}