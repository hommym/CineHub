package com.example.hommytv.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavTable::class,WatchLaterTable::class,HistoryTable::class],version=300)
abstract class AppDatabaseUpdate2(): RoomDatabase(){
    abstract fun databaseMethods():DatabaseMethods

    companion object{

        val migration1= object : Migration(200,300) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL("CREATE TABLE 'History_Table' ('contentTitle' TEXT,'imgUrl' TEXT,'contentId' INTEGER, 'mediaType' TEXT, 'id' INTEGER)")

            }
        }
        @Volatile
        var appDatabaseObject:AppDatabaseUpdate2?=null

        fun getAppDatabaseObject(context: Context):AppDatabaseUpdate2{

            return  appDatabaseObject?: synchronized(this){

                val appDatabaseObj= Room.databaseBuilder(context,AppDatabaseUpdate2::class.java,"App_Database").addMigrations(AppDatabaseUpdate1.migrationO,migration1).build()
                appDatabaseObject=appDatabaseObj
                appDatabaseObj
            }

        }



    }

}

