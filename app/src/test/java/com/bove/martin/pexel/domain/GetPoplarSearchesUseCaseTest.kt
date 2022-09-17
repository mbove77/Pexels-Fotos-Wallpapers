package com.bove.martin.pexel.domain

import com.bove.martin.pexel.data.database.PopularSearchesRepository
import com.bove.martin.pexel.domain.model.Search
import com.bove.martin.pexel.domain.usecases.GetPupularSearchesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Martín Bove on 22/6/2022.
 * E-mail: mbove77@gmail.com
 */
class GetPoplarSearchesUseCaseTest {

    @RelaxedMockK
    private lateinit var popularSearchesRepository: PopularSearchesRepository

    private lateinit var getPupularSearchesUseCase: GetPupularSearchesUseCase

    private val listOfSearches = listOf(
        Search(
            "desk",
            "escritorio",
            "https://images.pexels.com/photos/6471/woman-hand-smartphone-desk.jpg" +
                    "?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ),
        Search(
            "writing",
            "escribir",
            "https://images.pexels.com/photos/210661/pexels-photo-210661.jpeg" +
                    "?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ),
        Search(
            "coffee",
            "café",
            "https://images.pexels.com/photos/34079/pexels-photo.jpg" +
                    "?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ),
        Search(
            "desktop wallpaper",
            "desktop wallpaper",
            "https://images.pexels.com/photos/14676/pexels-photo-14676.png" +
                    "?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ),
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getPupularSearchesUseCase = GetPupularSearchesUseCase(popularSearchesRepository)
    }


    @Test
    fun `when call respond list from repository`() = runBlocking {
        //Given
        coEvery { popularSearchesRepository.getAllSearches() } returns listOfSearches

        //When
        val response = getPupularSearchesUseCase()

        //Then
        coVerify(exactly = 1) { popularSearchesRepository.getAllSearches() }
        Assert.assertTrue(response.size == 4)
    }
}
