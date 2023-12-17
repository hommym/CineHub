package com.example.hommytv

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore



private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name="LoginSessionDataStorage")

class App():Application() {

companion object{
    lateinit var objectOFDataStore:AppDataStore
}





    override fun onCreate() {
        super.onCreate()

        objectOFDataStore=AppDataStore(dataStore)
    }


}