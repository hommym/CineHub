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

class TheAdapterForListBasedRecyclerView (): RecyclerView.Adapter<TheAdapterForListBasedRecyclerView.Holder>() {
    var context: Context?=null
    var data= arrayListOf<MoviesList>()
    var section="Recent"

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        val poster: ImageView = itemView.findViewById(R.id.image_poster2)
        val movieTitle: TextView =itemView.findViewById(R.id.movie_title2)
        val genreTextView: TextView =itemView.findViewById(R.id.genre2)
        val optionMenu:ImageView=itemView.findViewById(R.id.options_menu)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_for_search_results_list_design,parent,false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currrentData= data[position]

        holder.apply {


            //            setting image resource
            val imgUri= currrentData.poster_path.toUri().buildUpon().scheme("https").build()
            poster.load(imgUri){
                placeholder(R.drawable.baseline_image_24)

            }

//            setting title
            when(currrentData.media_type){

                "movie"->{

                    movieTitle.text=currrentData.title
                }


                else->{
                    movieTitle.text=currrentData.original_name
                }


            }




//            setting the genre
            settingGenre(holder,currrentData)


//adding click listner to option icon

            optionMenu.setOnClickListener {


//   display context menu with add to favourite and watchlist as options(not yet implemented)



            }





        }






    }


    override fun getItemCount(): Int {
        return data.size
    }




    fun settingGenre(holder:Holder, data: MoviesList,){
//        the default for realeaseDate textView
        holder.genreTextView.text="Genre:"



        if(data.media_type=="movie"){
            for (id in data.genre_ids){
                for (genre in TheViewModel.genreForMovie?.genres!!){

                    if (id==genre.id){
                        holder.genreTextView.append("|${genre.name}")
                        break
                    }
                }

            }

        }

        else{

            for (id in data.genre_ids){
                for (genre in TheViewModel.genreForSeries?.genres!!){

                    if (id==genre.id){
                        holder.genreTextView.append("|${genre.name}")
                        break
                    }
                }

            }

        }







    }

}