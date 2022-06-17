package com.bove.martin.pexel.domain

import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.data.network.FotosRepository
import javax.inject.Inject

/**
 * Created by Martín Bove on 14/6/2022.
 * E-mail: mbove77@gmail.com
 */
class GetFotosUseCase @Inject constructor(private val fotosRepository: FotosRepository) {
    suspend operator fun invoke(queryString: String?, pageNumber: Int, resetList: Boolean): OperationResult {
        return fotosRepository.getCuratedFotos(queryString, pageNumber, resetList)
    }
}