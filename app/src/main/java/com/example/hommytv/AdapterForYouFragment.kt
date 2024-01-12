package com.example.hommytv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load

class AdapterForYouFragment :RecyclerView.Adapter<AdapterForYouFragment.Holder>() {

    var context:Context?=null
    var data= arrayListOf<DataHolder>()


    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        val image:ImageView= itemView.findViewById(R.id.img_you_frag)
        val title:TextView=itemView.findViewById(R.id.title_you_frag)
        val mediaType:TextView=itemView.findViewById(R.id.media_type_you_frag)
        val item:ConstraintLayout=itemView.findViewById(R.id.item_you_frag)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view:View= LayoutInflater.from(context).inflate(R.layout.content_you_fragment_design,parent,false)

        return Holder(view)
    }

    override fun getItemCount(): Int {

     return if(data.size>10){
         10
     }
        else{
            data.size
     }


    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentData= data[position]
        holder.apply {
            val imgUri= currentData.imgUrl.toUri().buildUpon().scheme("https").build()
            image.load(imgUri){
                placeholder(R.drawable.baseline_image_24)

            }

            title.text=currentData.contentTitle
            mediaType.text=currentData.mediaType

            item.setOnClickListener {


//                not yet implemented

            }

        }
    }
}