package com.bove.martin.pexel.domain.model

import com.bove.martin.pexel.data.database.entities.SearchEntity

/**
 * Created by Martín Bove on 12-Feb-20.
 * E-mail: mbove77@gmail.com
 */
data class Search(var searchInEnglish: String, var searchInSpanish: String, var photo: String)

fun SearchEntity.toModel() = Search(searchInEnglish, searchInSpanish, photo)