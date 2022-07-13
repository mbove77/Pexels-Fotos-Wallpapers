package com.bove.martin.pexel.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bove.martin.pexel.domain.GetFotosUseCase
import com.bove.martin.pexel.domain.GetPupularSearchesUseCase
import com.bove.martin.pexel.domain.GetSearchedFotosUseCase
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.domain.model.Search
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
class MainActivityViewModelTest {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    @RelaxedMockK
    private lateinit var getFotosUseCase: GetFotosUseCase
    @RelaxedMockK
    private lateinit var getPupularSearchesUseCase: GetPupularSearchesUseCase
    @RelaxedMockK
    private lateinit var getSearchedFotosUseCase: GetSearchedFotosUseCase
    @RelaxedMockK
    private lateinit var foto: Foto
    @RelaxedMockK
    private lateinit var search: Search

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        mainActivityViewModel = MainActivityViewModel(
            getFotosUseCase,
            getPupularSearchesUseCase,
            getSearchedFotosUseCase,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `when call getFotos with null queryString run getCuratedPhotos`() = runTest {
        //Given
        coEvery { getFotosUseCase(1) } returns OperationResult(true, null, listOf(foto))

        //When
        mainActivityViewModel.getFotos()

        //Then
        coVerify(exactly = 1) { getFotosUseCase(1) }
        Assert.assertTrue(mainActivityViewModel.fotos.value?.size == 1)
    }

    @Test
    fun `when call getFotos with null queryString and returns error`() = runTest {
        //Given
        coEvery { getFotosUseCase(1) } returns OperationResult(false, null, null)

        //When
        mainActivityViewModel.getFotos()

        //Then
        coVerify(exactly = 1) { getFotosUseCase(1) }
        Assert.assertNull(mainActivityViewModel.fotos.value)
    }

    @Test
    fun `when call getFotos with queryString run getSearchedFotos`() = runTest {
        //Given
        coEvery { getSearchedFotosUseCase("car", 1)} returns OperationResult(true, null, listOf(foto))
        mainActivityViewModel.setQueryString("car")

        //When
        mainActivityViewModel.getFotos()

        //Then
        coVerify(exactly = 0) { getFotosUseCase(1) }
        coVerify(exactly = 1) { getSearchedFotosUseCase("car", 1) }
        Assert.assertTrue(mainActivityViewModel.fotos.value?.size == 1)
    }

    @Test
    fun `when call getFotos with queryString and return error`() = runTest {
        //Given
        coEvery { getSearchedFotosUseCase("car", 1)} returns OperationResult(false, null, null)
        mainActivityViewModel.setQueryString("car")

        //When
        mainActivityViewModel.getFotos()

        //Then
        coVerify(exactly = 0) { getFotosUseCase(1) }
        coVerify(exactly = 1) { getSearchedFotosUseCase("car", 1) }
        Assert.assertNull(mainActivityViewModel.fotos.value)
    }

    @Test
    fun `when call getSearchOptions set searches liveData with a list`() = runTest {
        //Given
        coEvery { getPupularSearchesUseCase() } returns listOf(search)

        //When
        mainActivityViewModel.getSearchOptions()

        //Then
        coVerify(exactly = 1) { getPupularSearchesUseCase() }
        Assert.assertTrue(mainActivityViewModel.searches.value?.size == 1 )
    }


    @Test
    fun `when call setQueryString set queryString var`() = runTest {
        //Given
        val queryStringTest = "test string"

        //When
        mainActivityViewModel.setQueryString(queryStringTest)

        //Then
        Assert.assertTrue(mainActivityViewModel.getQueryString().equals(queryStringTest))
    }


    @Test
    fun `when call setQueryString with null don't set queryString`() = runTest {
        //Given
        val queryStringTest = "test string"

        //When
        mainActivityViewModel.setQueryString(queryStringTest)
        mainActivityViewModel.setQueryString(null)

        //Then
        Assert.assertTrue(mainActivityViewModel.getQueryString().equals(queryStringTest))
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

}