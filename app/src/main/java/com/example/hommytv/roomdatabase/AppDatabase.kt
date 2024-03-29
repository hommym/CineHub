package com.example.hommytv.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase





@Database(entities = [FavTable::class,WatchLaterTable::class,HistoryTable::class,PlayListNameTable::class,PlayListItemTable::class],version=100)
abstract class AppDatabase:RoomDatabase() {


      abstract fun databaseMethods():DatabaseMethods



      companion object{
         @Volatile
         var appDatabaseObject:AppDatabase?=null

          fun getAppDatabaseObject(context:Context):AppDatabase{

              return  appDatabaseObject?: synchronized(this){

                  val appDatabaseObj= Room.databaseBuilder(context,AppDatabase::class.java,"App_Database").build()
                  appDatabaseObject=appDatabaseObj
                  appDatabaseObj
              }

          }



      }
}

