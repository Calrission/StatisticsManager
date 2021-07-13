package com.example.statisticmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.statisticmanager.common.ModelMatch

@Dao
interface RoomDao {
    @Insert
    fun insert(modelMatch: ModelMatch)

    @get:Query("SELECT * FROM `match`")
    val allModelMatch: LiveData<List<ModelMatch>>

    @Update
    fun update(modelMatch: ModelMatch)

    @Delete
    fun delete(modelMatch: ModelMatch)
}
