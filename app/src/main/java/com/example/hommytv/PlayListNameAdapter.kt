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
var hasPlayListBeenSelected:Boolean=false,var selectedPlayList:String="",val flowForUpdatedPlaylistName:MutableSharedFlow<PlayListNameTable>): RecyclerView.Adapter<PlayListNameAdapter.Holder>()  {



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

        var dataToBeSavedInTable= PlayListItemTable(currentData.name,dataInDatabase.contentTitle,dataInDatabase.imgUrl,dataInDatabase.contentId,dataInDatabase.mediaType)
        viewModel.showPlayListItem().observe((context as AppCompatActivity), Observer {


            for(item in it){

                if(item.name==currentData.name && item.contentTitle== dataInDatabase.contentTitle){
                    dataToBeSavedInTable=item
                    break

                }
            }


        })
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->


            if(hasPlayListBeenSelected&&selectedPlayList!=currentData.name){

                buttonView.isChecked=false
            }
            else{
                if(isChecked){

                    //                saving play list item in play_list_item_table
                    context.lifecycleScope.launch {
                        viewModel.addToPlayListItem(dataToBeSavedInTable)
                        currentData.imageToShowOnPlaylist=dataToBeSavedInTable.imgUrl
                        currentData.numberOfItems++
                        hasPlayListBeenSelected=true
                        selectedPlayList=dataToBeSavedInTable.name

                    }


                    Toast.makeText(context,"Added PlayList",Toast.LENGTH_SHORT).show()

                }
                else{
//                removing play list item from  play_list_item_table
                    context.lifecycleScope.launch {
                        viewModel.removePLaylistItem(dataToBeSavedInTable)
                        currentData.numberOfItems--
                        hasPlayListBeenSelected=false
                        selectedPlayList=""
                    }


                    Toast.makeText(context,"Remove  PlayList",Toast.LENGTH_SHORT).show()
                }
            }


            context.lifecycleScope.launch {
                flowForUpdatedPlaylistName.emit(currentData)
            }

        }



    }

}