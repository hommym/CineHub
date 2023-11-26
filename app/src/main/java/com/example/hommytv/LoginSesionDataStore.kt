package com.example.hommytv

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit

class LoginSesionDataStore(val dataStore:DataStore<Preferences>) {

    companion object{
        val key= booleanPreferencesKey("hasLoginSessionExpired")
    }


    suspend fun writeData(data:Boolean){

        dataStore.edit { dataInDataStore->
            dataInDataStore[key]=data
        }

    }

val readData=dataStore.data

}