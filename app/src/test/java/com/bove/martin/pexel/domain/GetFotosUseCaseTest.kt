package com.bove.martin.pexel.domain

import com.bove.martin.pexel.data.network.FotosRepository
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.presentation.utils.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test


/**
 * Created by Martín Bove on 18/6/2022.
 * E-mail: mbove77@gmail.com
 */
class GetFotosUseCaseTest {

    @RelaxedMockK
    private lateinit var fotosRepository: FotosRepository

    @RelaxedMockK
    private lateinit var foto: Foto

    private lateinit var getFotosUseCase: GetFotosUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getFotosUseCase = GetFotosUseCase(fotosRepository)
    }

    @Test
    fun `when call with pageNumber 0 return error`() = runBlocking {
        //Given
        coEvery { fotosRepository.getCuratedFotos(0) } returns OperationResult(false, UiText.DynamicString("Test"), null)

        //When
        val response = getFotosUseCase(0)

        //Then
        coVerify(exactly = 1) { fotosRepository.getCuratedFotos(0) }
        Assert.assertFalse(response.operationResult)
        Assert.assertNotNull(response.resultMensaje)
    }

    @Test
    fun `when call with pageNumber 1 return list`() = runBlocking {
        //Given
        val fotoList = listOf(foto)
        coEvery { fotosRepository.getCuratedFotos(1) } returns OperationResult(true, null, fotoList)

        //When
        val response = getFotosUseCase(1)

        //Then
        coVerify(exactly = 1) { fotosRepository.getCuratedFotos(1) }
        Assert.assertTrue(response.operationResult)
        Assert.assertNull(response.resultMensaje)
        Assert.assertTrue((response.resultObject as List<*>).isNotEmpty())
    }
}