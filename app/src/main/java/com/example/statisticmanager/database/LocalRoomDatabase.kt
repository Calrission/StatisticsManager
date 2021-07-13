package com.example.statisticmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.statisticmanager.common.ConverterModelMatch
import com.example.statisticmanager.common.ModelMatch

@Database(entities = [ModelMatch::class], version = 1)
@TypeConverters(ConverterModelMatch::class)
abstract class LocalRoomDatabase: RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object{
        @Volatile
        private var roomInstance: LocalRoomDatabase? = null

        internal  fun getDatabase(context: Context): LocalRoomDatabase? {
            if (roomInstance == null){
                synchronized(LocalRoomDatabase::class.java){
                    if (roomInstance == null){
                        roomInstance = Room.databaseBuilder(
                            context.applicationContext,
                            LocalRoomDatabase::class.java,
                            "db"
                        ).build()
                    }
                }
            }
            return roomInstance
        }
    }
}