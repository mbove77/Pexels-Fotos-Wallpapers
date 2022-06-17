package com.bove.martin.pexel.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bove.martin.pexel.domain.model.Search

/**
 * Created by Martín Bove on 16/6/2022.
 * E-mail: mbove77@gmail.com
 */

@Entity(tableName = "searches_table")
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "searchInEnglish") var searchInEnglish: String,
    @ColumnInfo(name = "searchInSpanish") var searchInSpanish: String,
    @ColumnInfo(name = "photo") var photo: String
    )


fun Search.toDatabase() = SearchEntity(searchInEnglish = searchInEnglish, searchInSpanish = searchInSpanish, photo = photo)