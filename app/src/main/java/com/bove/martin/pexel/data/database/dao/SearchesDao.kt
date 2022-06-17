package com.bove.martin.pexel.data.database.dao

import androidx.room.*
import com.bove.martin.pexel.data.database.entities.SearchEntity

/**
 * Created by Martín Bove on 16/6/2022.
 * E-mail: mbove77@gmail.com
 */

@Dao
interface SearchesDao {

    @Query("SELECT COUNT(*) FROM searches_table")
    suspend fun getSearchesCount() : Int

    @Query("SELECT * FROM searches_table")
    suspend fun getAll(): List<SearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(searches: List<SearchEntity>)

    @Query("DELETE FROM searches_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM searches_table WHERE id = :searchId")
    suspend fun getSearch(searchId: Int) : SearchEntity

    @Insert
    suspend fun insert(searchEntity: SearchEntity)

    @Update
    suspend fun update(searchEntity: SearchEntity)

    @Delete
    suspend fun delete(searchEntity: SearchEntity)
}