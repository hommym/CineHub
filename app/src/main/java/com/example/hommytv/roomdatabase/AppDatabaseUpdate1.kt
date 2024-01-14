package com.example.hommytv.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [FavTable::class,WatchLaterTable::class,HistoryTable::class],version=200)
abstract class AppDatabaseUpdate1(): RoomDatabase(){
    abstract fun databaseMethods():DatabaseMethods

    companion object{
        val migrationO= object : Migration(100,200) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL("ALTER TABLE fav_table ADD COLUMN mediaType TEXT NOT NULL DEFAULT movie")
                db.execSQL("ALTER TABLE watch_later_table ADD COLUMN mediaType TEXT NOT NULL DEFAULT movie")
            }
        }
        @Volatile
        var appDatabaseObject:AppDatabaseUpdate1?=null

        fun getAppDatabaseObject(context: Context):AppDatabaseUpdate1{

            return  appDatabaseObject?: synchronized(this){

                val appDatabaseObj= Room.databaseBuilder(context,AppDatabaseUpdate1::class.java,"App_Database").addMigrations(migrationO).build()
                appDatabaseObject=appDatabaseObj
                appDatabaseObj
            }

        }



    }

}

