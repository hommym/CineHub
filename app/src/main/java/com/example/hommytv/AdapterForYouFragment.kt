package com.example.hommytv

import android.content.Context
import android.content.Intent
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
import com.example.hommytv.roomdatabase.PlayListNameTable

class AdapterForYouFragment :RecyclerView.Adapter<AdapterForYouFragment.Holder>() {

    var context:Context?=null
    var data= arrayListOf<DataHolder>()

//    the purpose of dataBeingShown is to help differentiate between data from history,fav ,watchlater and playlist
    var dataBeingShown="History"

//    playListTitle holds the title of a playlist if this adapter is used for the playlist recycler view
    var playListTitle= listOf<PlayListNameTable>()



    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        val image2:ImageView= itemView.findViewById(R.id.img_you_frag)
        val image1:ImageView=itemView.findViewById(R.id.img2_you_frag)
        val topImageLayout:ConstraintLayout=itemView.findViewById(R.id.top_image_layout)
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
            if(dataBeingShown!="PlayList"){
                data.size
            }
         else{
             playListTitle.size
            }

     }


    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.apply {






            if(dataBeingShown!="PlayList"){
                val currentData= data[position]
                val imgUri= currentData.imgUrl.toUri().buildUpon().scheme("https").build()
                image2.load(imgUri){
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
            else{
//                making top image visible
                topImageLayout.visibility=View.VISIBLE

                val currentPlayListName=playListTitle[position]

//                setting default images to playlist with no data
                image2.setImageResource(R.drawable.baseline_image_24)
                image1.setImageResource(R.drawable.baseline_image_24)

//                conditions for setting image
                if(currentPlayListName.numberOfItems==1){
                    val imgUri= currentPlayListName.imageToShowOnPlaylist.toUri().buildUpon().scheme("https").build()
                    image2.load(imgUri){
                        placeholder(R.drawable.baseline_image_24)


                    }
                }
                else if(currentPlayListName.numberOfItems>1){
                    val imgUri= currentPlayListName.imageToShowOnPlaylist.toUri().buildUpon().scheme("https").build()
                    image2.load(imgUri){
                        placeholder(R.drawable.baseline_image_24)

                    }

                    val imgUri1= currentPlayListName.imageToShowOnPlaylist2.toUri().buildUpon().scheme("https").build()
                    image1.load(imgUri1){
                        placeholder(R.drawable.baseline_image_24)

                    }


                }

                title.text=currentPlayListName.name
                mediaType.text=currentPlayListName.playListMediaType



                item.setOnClickListener{
//                    not yet implemented
                    val intent= Intent(context,ActivityForDisplayingSearchResults::class.java)
                    intent.putExtra("YouFragmentSection","Playlist")
                    intent.putExtra("PlaylistName",currentPlayListName.name)
                    context?.startActivity(intent)
                }

                optionMenu.setOnClickListener{
//                    not yet implemented
                }

            }





        }
    }
}