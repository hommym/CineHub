package com.example.hommytv.roomdatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseMethods {

    @Insert
    suspend fun addToFav(data:FavTable)
    @Delete
    suspend fun removeFromFav(data:FavTable)
    @Query("SELECT * FROM fav_table")
     fun showFav():Flow<List<FavTable>>





    @Insert
    suspend fun addToWatchLater(data:WatchLaterTable)
    @Delete
    suspend fun removeFromWatchLater(data:WatchLaterTable)
    @Query("SELECT * FROM watch_later_table")
     fun showWatchLater():Flow<List<WatchLaterTable>>





     @Insert
    suspend fun addToHistory(data:HistoryTable)
     @Query("SELECT * FROM History_Table")
     fun showHistory():Flow<List<HistoryTable>>
     @Delete
    suspend fun removeFromHistory(data:HistoryTable)





    @Insert
    suspend fun addToPlaylistName(data:PlayListNameTable)
    @Query("SELECT * FROM play_list_name_table")
    fun showPlaylistNames():Flow<List<PlayListNameTable>>

    @Update
    suspend fun updatePlaylistName(data:PlayListNameTable)


    @Insert
    suspend fun addToPlaylistItem(data:PlayListItemTable)
    @Query("SELECT * FROM play_list_item_table")
    fun showPlaylistItems():Flow<List<PlayListItemTable>>

    @Delete
    suspend fun removePlayListItem(data:PlayListItemTable)

}