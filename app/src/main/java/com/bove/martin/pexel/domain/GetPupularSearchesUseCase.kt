package com.bove.martin.pexel.domain

import com.bove.martin.pexel.data.database.PopularSearchesRepository
import com.bove.martin.pexel.domain.model.Search
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Martín Bove on 14/6/2022.
 * E-mail: mbove77@gmail.com
 */

@Singleton
class GetPupularSearchesUseCase @Inject constructor(private val popularSearchesRepository: PopularSearchesRepository) {
    private var popularSearchesCache: List<Search> = listOf()

    suspend operator fun invoke(): List<Search> {
        if(popularSearchesCache.isEmpty()) {
            popularSearchesCache = popularSearchesRepository.getAllSearches()
        }
        return popularSearchesCache.shuffled()
    }
}