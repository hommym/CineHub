package com.example.hommytv.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "play_list_name_table")
class PlayListNameTable(var name:String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int =0
}