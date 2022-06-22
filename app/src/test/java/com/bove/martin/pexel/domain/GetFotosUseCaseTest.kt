package com.bove.martin.pexel.domain

import com.bove.martin.pexel.AppConstants
import com.bove.martin.pexel.data.network.FotosRepository
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.OperationResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
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
        coEvery { fotosRepository.getCuratedFotos(0) } returns OperationResult(false, AppConstants.AppErrors.PAGING_ERROR.getErrorMessage(), null)

        val response = getFotosUseCase(0)

        coVerify(exactly = 1) { fotosRepository.getCuratedFotos(0) }
        assert(!response.operationResult)
        assert(response.resultMensaje != null)
        if (!response.resultMensaje.isNullOrEmpty())
            assert(response.resultMensaje == AppConstants.AppErrors.PAGING_ERROR.getErrorMessage())
    }

    @Test
    fun `when call with pageNumber 1 return list`() = runBlocking {
        val fotoList = listOf(foto)
        coEvery { fotosRepository.getCuratedFotos(1) } returns OperationResult(true, null, fotoList)

        val response = getFotosUseCase(1)

        coVerify(exactly = 1) { fotosRepository.getCuratedFotos(1) }
        assert(response.operationResult)
        assert(response.resultMensaje == null)
        assert((response.resultObject as List<Foto>).isNotEmpty())
    }
}