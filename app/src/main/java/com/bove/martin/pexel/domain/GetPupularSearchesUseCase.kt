package com.bove.martin.pexel.domain

import com.bove.martin.pexel.data.database.PopularSearchesRepository
import javax.inject.Inject

/**
 * Created by Martín Bove on 14/6/2022.
 * E-mail: mbove77@gmail.com
 */
class GetPupularSearchesUseCase @Inject constructor(private val popularSearchesRepository: PopularSearchesRepository) {
    suspend operator fun invoke() = popularSearchesRepository.getAllSearches()
}