package com.bove.martin.pexel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bove.martin.pexel.data.database.dao.SearchesDao
import com.bove.martin.pexel.data.database.entities.SearchEntity

/**
 * Created by Martín Bove on 16/6/2022.
 * E-mail: mbove77@gmail.com
 */

@Database(entities = [SearchEntity::class], version = 1, exportSchema = false)
abstract class SearchesDatabase: RoomDatabase() {

    abstract fun getSearchesDato(): SearchesDao
}