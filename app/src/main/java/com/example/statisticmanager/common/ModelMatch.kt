package com.example.statisticmanager.common

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Entity(tableName = "match")
data class ModelMatch (
    @field:PrimaryKey
    val id: String,
    val team_1: String,
    val team_2: String,
    var team_1_win: Int,
    var team_2_win: Int,
    @TypeConverters(ConverterModelMatch::class)
    var listState: MutableList<ModelState>
)

class ConverterModelMatch{
    @TypeConverter
    fun stringToListModelState(string: String): List<ModelState> {
        if (string.isEmpty()){
            return listOf()
        }
        val type: Type = object: TypeToken<List<ModelState>>() {}.type
        return Gson().fromJson(string, type)
    }
    @TypeConverter
    fun listModelStateToString(listState: List<ModelState>): String {
        return Gson().toJson(listState)
    }
}
