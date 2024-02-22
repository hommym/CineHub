package com.example.hommytv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.hommytv.roomdatabase.PlayListItemTable
import com.example.hommytv.roomdatabase.PlayListNameTable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class PlayListNameAdapter(val context:Context,
var data:List<PlayListNameTable>,var dataInDatabase: DataHolder,
val viewModel:TheViewModel,
var hasPlayListBeenSelected:Boolean=false,var selectedPlayList:String="",val flowForUpdatedPlaylistName:MutableSharedFlow<PlayListSheetFrag.Companion.PlaylistNameAndItemHolder?>): RecyclerView.Adapter<PlayListNameAdapter.Holder>()  {



    class Holder(itemView: View):RecyclerView.ViewHolder(itemView){

    val checkBox:CheckBox=itemView.findViewById(R.id.play_list_checkbox)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view:View= LayoutInflater.from(context).inflate(R.layout.play_list_name_design,parent,false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currentData= data[position]

        holder.checkBox.text=currentData.name


        if(currentData.playListMediaType==""){
            currentData.playListMediaType=dataInDatabase.mediaType


        }
        else if(currentData.playListMediaType!=dataInDatabase.mediaType && !currentData.playListMediaType.contains('&')){
            currentData.playListMediaType="${currentData.playListMediaType}&${dataInDatabase.mediaType}"

        }

        val dataToBeSavedInTable= PlayListItemTable(currentData.name,dataInDatabase.contentTitle,dataInDatabase.imgUrl,dataInDatabase.contentId,dataInDatabase.mediaType)


        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->


            if(hasPlayListBeenSelected&&selectedPlayList!=currentData.name){

                buttonView.isChecked=false
            }
            else{
                if(isChecked){

                    //                saving play list item in play_list_item_table
                        currentData.imageToShowOnPlaylist2=currentData.imageToShowOnPlaylist
                        currentData.imageToShowOnPlaylist=dataToBeSavedInTable.imgUrl
                        currentData.numberOfItems++
                        hasPlayListBeenSelected=true
                        selectedPlayList=dataToBeSavedInTable.name
                    (context as AppCompatActivity).lifecycleScope.launch {

                                flowForUpdatedPlaylistName.emit(PlayListSheetFrag.Companion.PlaylistNameAndItemHolder(currentData,dataToBeSavedInTable))
                        }


                }
                else{
//                removing play list item from  play_list_item_table

                        currentData.numberOfItems--
                        hasPlayListBeenSelected=false
                        selectedPlayList=""
                    (context as AppCompatActivity).lifecycleScope.launch {
                            flowForUpdatedPlaylistName.emit(null)
                        }

                }
            }




        }



    }

}