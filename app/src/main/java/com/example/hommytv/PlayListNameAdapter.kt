package com.example.hommytv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hommytv.roomdatabase.PlayListItemTable
import com.example.hommytv.roomdatabase.PlayListNameTable

class PlayListNameAdapter(val context:Context,var data:List<PlayListNameTable>,var dataInDatabase: DataHolder): RecyclerView.Adapter<PlayListNameAdapter.Holder>()  {



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


        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->

            val dataToBeSavedInTable= PlayListItemTable(currentData.name,dataInDatabase.contentTitle,dataInDatabase.imgUrl,dataInDatabase.contentId,dataInDatabase.mediaType)
            if(isChecked){
//                saving play list item in play_list_item_table(not yet implemented)
                Toast.makeText(context,"Added PlayList",Toast.LENGTH_SHORT).show()

            }
            else{
//                removing play list item from  play_list_item_table(not yet implemented)
                Toast.makeText(context,"Remove  PlayList",Toast.LENGTH_SHORT).show()
            }

        }

    }

}