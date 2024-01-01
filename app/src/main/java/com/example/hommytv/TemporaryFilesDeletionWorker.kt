package com.example.hommytv

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.File


class TemporaryFilesDeletionWorker(val appContext: Context, val workerParams: WorkerParameters):
    CoroutineWorker(appContext,workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val fileObjOfImagesForSlider= File(appContext.filesDir,"ImagesForSlider")

        for(file in fileObjOfImagesForSlider.listFiles()!!){
//            deleting folder content
            file.delete()

        }

//        deleting folder
        fileObjOfImagesForSlider.delete()


        return Result.success()
    }
}