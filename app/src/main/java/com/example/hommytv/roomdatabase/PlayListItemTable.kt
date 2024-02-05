package com.example.hommytv.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_list_item_table")
class PlayListItemTable (var name:String,var contentTitle:String,var imgUrl:String, var contentId:Int, var mediaType:String){
    @PrimaryKey(autoGenerate = true)
    var id=0
}