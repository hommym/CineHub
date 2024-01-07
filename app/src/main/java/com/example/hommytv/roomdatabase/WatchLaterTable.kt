package com.example.hommytv.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_later_table")
class WatchLaterTable(var contentTitle:String,var imgUrl:String, var contentId:Int, var mediaType:String) {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}