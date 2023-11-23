package com.example.hommytv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hommytv.retrofitdataclasses.MoviesList

class TheAdapter():RecyclerView.Adapter<TheAdapter.Holder>() {
   var context:Context?=null
   var data= arrayListOf<MoviesList>()
    var section="Recent"

    class Holder(itemView: View):RecyclerView.ViewHolder(itemView){

        val poster:ImageView= itemView.findViewById(R.id.image_poster)
        val movieTitle:TextView=itemView.findViewById(R.id.movie_title)

//        (Somtimes we use this textView for holding genre)
        val releaseDate:TextView=itemView.findViewById(R.id.releaseDate)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view:View= LayoutInflater.from(context).inflate(R.layout.design_for_items,parent,false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currrentData= data[position]

        holder.apply {

//            setting image resource
            val imgUri= currrentData.poster_path.toUri().buildUpon().scheme("https").build()
            poster.load(imgUri){
                placeholder(R.drawable.baseline_image_24)

            }

//            checking f adapter is being used in main activity
            if(context is MainActivity){

//                setting the title and release dates or genres
              when(section){
                  "Recent"->{

                      settingGenre(holder,currrentData,"Movie")
                      movieTitle.text=currrentData.title

                  }

                  "Top Rated"->{

                      settingGenre(holder,currrentData,"Series")
                      movieTitle.text=currrentData.original_name

                  }

                  "Upcoming"->{

                      releaseDate.text = "Release Date:${currrentData.release_date}"
                      movieTitle.text=currrentData.title
                  }


                  else->{

                      settingGenre(holder,currrentData,"Series")
                      movieTitle.text=currrentData.original_name
                  }


              }

            }






        }


    }


     fun settingGenre(holder:Holder, data:MoviesList, genreType:String){
//        the default for realeaseDate textView
        holder.releaseDate.text="Genre:"

         if (genreType=="Movie"){

             for (id in data.genre_ids){
                 for (genre in TheViewModel.genreForMovie?.genres!!){

                     if (id==genre.id){
                         holder.releaseDate.append("|${genre.name}")
                         break
                     }
                 }

             }

         }
         else{
             for (id in data.genre_ids){
                 for (genre in TheViewModel.genreForSeries?.genres!!){

                     if (id==genre.id){
                         holder.releaseDate.append("|${genre.name}")
                         break
                     }
                 }

             }

         }



    }

}