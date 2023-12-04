package com.example.hommytv

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


class LoginSessionBackgroundWork(val appContext:Context,val workerParams:WorkerParameters):CoroutineWorker(appContext,workerParams) {
    override suspend fun doWork(): Result {
        // setting the key  hasLoginSessionExpired in LogInSession data store
        val dataStoreObject= App.objectOFLogInSessionDataStore
        dataStoreObject.writeData(false)

        return Result.success()
    }


}