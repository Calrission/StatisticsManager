package com.example.statisticmanager.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.statisticmanager.common.ModelMatch

class RoomViewModel(application: Application): AndroidViewModel(application) {
    private val dao: RoomDao
    internal val allMatch: LiveData<List<ModelMatch>>

    init {
        val db = LocalRoomDatabase.getDatabase(application)
        dao = db!!.roomDao()
        allMatch = dao.allModelMatch
    }

    fun update(modelMatch: ModelMatch){
        UpdateAsyncTask(dao).execute(modelMatch)
    }

    fun insert(modelMatch: ModelMatch){
        InsertAsyncTask(dao).execute(modelMatch)
    }

    fun delete(modelMatch: ModelMatch){
        DeleteAsyncTask(dao).execute(modelMatch)
    }

    companion object{
        private class InsertAsyncTask(private val dao: RoomDao): AsyncTask<ModelMatch, Void, Void>(){
            override fun doInBackground(vararg params: ModelMatch): Void? {
                dao.insert(params[0])
                return null
            }
        }

        private class UpdateAsyncTask(private val dao: RoomDao): AsyncTask<ModelMatch, Void, Void>(){
            override fun doInBackground(vararg params: ModelMatch): Void? {
                dao.update(params[0])
                return null
            }
        }

        private class DeleteAsyncTask(private val dao: RoomDao): AsyncTask<ModelMatch, Void, Void>(){
            override fun doInBackground(vararg params: ModelMatch): Void? {
                dao.delete(params[0])
                return null
            }
        }
    }


}