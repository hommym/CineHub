package com.example.hommytv

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit

class AppDataStore(val dataStore:DataStore<Preferences>) {

    companion object{
        val logInSessionKey= booleanPreferencesKey("hasLoginSessionExpired")
        val isProfileSet= booleanPreferencesKey("isProfileSet")
    }


    suspend fun writeDataToLogInSession(data:Boolean){

        dataStore.edit { dataInDataStore->
            dataInDataStore[logInSessionKey]=data
        }

    }

    suspend fun writeDataToIsProfileSet(data:Boolean){

        dataStore.edit { dataInDataStore->
            dataInDataStore[isProfileSet]=data
        }

    }

val readData=dataStore.data

}