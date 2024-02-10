package com.example.hommytv.roomdatabase

import kotlinx.coroutines.flow.Flow

class Repository( var databaseMethods:DatabaseMethods) {


    suspend fun addToFav(data:FavTable){

        databaseMethods.addToFav(data)

    }


    suspend fun removeFromFav(data:FavTable){

        databaseMethods.removeFromFav(data)
    }


     fun showFav():Flow<List<FavTable>>{
        return databaseMethods.showFav()
    }




    suspend fun addToWatchLater(data:WatchLaterTable){

        databaseMethods.addToWatchLater(data)

    }


    suspend fun removeFromWatchLater(data:WatchLaterTable){

        databaseMethods.removeFromWatchLater(data)
    }


    fun showWatchLater():Flow<List<WatchLaterTable>>{
        return databaseMethods.showWatchLater()
    }

    suspend fun addToHistory(data:HistoryTable){
        databaseMethods.addToHistory(data)
    }

    fun showHistory():Flow<List<HistoryTable>>{

        return databaseMethods.showHistory()
    }

    suspend fun removeFromHistory(data:HistoryTable){
        databaseMethods.removeFromHistory(data)
    }


    suspend fun addToPlaylistName(data:PlayListNameTable){

        databaseMethods.addToPlaylistName(data)
    }

    fun showPlaylistNames():Flow<List<PlayListNameTable>>{

        return databaseMethods.showPlaylistNames()
    }

    suspend fun updatePlaylistName(data:PlayListNameTable){

        databaseMethods.updatePlaylistName(data)
    }

    suspend fun addToPlaylistItem(data:PlayListItemTable){
        databaseMethods.addToPlaylistItem(data)
    }
    fun showPlaylistItems():Flow<List<PlayListItemTable>>{
        return  databaseMethods.showPlaylistItems()
    }

    suspend fun removePlayListItem(data:PlayListItemTable){
        databaseMethods.removePlayListItem(data)
    }

}