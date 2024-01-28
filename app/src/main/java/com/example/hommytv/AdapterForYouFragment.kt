package com.example.hommytv

import android.content.Context
import android.os.Bundle
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
//    the purpose of dataBeingShown is to help differentiate between data from history and playlist
    var dataBeingShown="History"

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        val image:ImageView= itemView.findViewById(R.id.img_you_frag)
        val title:TextView=itemView.findViewById(R.id.title_you_frag)
        val mediaType:TextView=itemView.findViewById(R.id.media_type_you_frag)
        val item:ConstraintLayout=itemView.findViewById(R.id.item_you_frag)
        val optionMenu:ImageView=itemView.findViewById(R.id.menu_item_you_frag)

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




                //       changing fragment to details fragment(SelectedMovieOrSeriesFragment)
                val fragTransaction= (context as MainActivity).supportFragmentManager.beginTransaction()
                fragTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.slide_in,R.anim.slide_out)
                val nextFrag=SelectedMoviesOrSeriesFragment()
                val bundleObject= Bundle()
                bundleObject.putString("MediaType",currentData.mediaType)
                bundleObject.putString("Poster",currentData.imgUrl)
                bundleObject.putInt("Movie_Id",currentData.contentId)
                nextFrag.arguments=bundleObject

                fragTransaction.replace(R.id.layout_for_sections,nextFrag)
                fragTransaction.addToBackStack(null)

                fragTransaction.commit()

            }


            optionMenu.setOnClickListener {

                val modalSheetObj= SearchResultsButtomSheet(dataBeingShown,(context as MainActivity).viewModel,currentData)
                modalSheetObj.show((context as MainActivity).supportFragmentManager,SearchResultsButtomSheet.TAG)



            }

        }
    }
}