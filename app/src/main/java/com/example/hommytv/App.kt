package com.example.hommytv

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.hommytv.roomdatabase.AppDatabaseUpdate2
import com.example.hommytv.roomdatabase.Repository


private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name="LoginSessionDataStorage")

class App():Application() {

companion object{
    lateinit var objectOFDataStore:AppDataStore

}


private val databaseObject: AppDatabaseUpdate2 by lazy {
    AppDatabaseUpdate2.getAppDatabaseObject(this)
}

val repositoryObject:Repository by lazy {
    Repository(databaseObject.databaseMethods())
}




    override fun onCreate() {
        super.onCreate()

        objectOFDataStore=AppDataStore(dataStore)
    }


}