package com.example.hommytv

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hommytv.retrofitdataclasses.MoviesList
import kotlinx.coroutines.launch

class TheAdapterForListBasedRecyclerView (): RecyclerView.Adapter<TheAdapterForListBasedRecyclerView.Holder>() {
    var context: Context?=null
    var data= arrayListOf<MoviesList>()


    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        val poster: ImageView = itemView.findViewById(R.id.image_poster2)
        val movieTitle: TextView =itemView.findViewById(R.id.movie_title2)
        val genreTextView: TextView =itemView.findViewById(R.id.genre2)
        val optionMenu:ImageView=itemView.findViewById(R.id.options_menu)
        val item:CardView=itemView.findViewById(R.id.itemSelector)


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

//            setting listener for when an item is selected

            item.setOnClickListener {


                //       changing fragment to details fragment(SelectedMovieOrSeriesFragment)
                val fragTransaction= (context as ActivityForDisplayingSearchResults).supportFragmentManager.beginTransaction()
                fragTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.slide_in,R.anim.slide_out)
                val nextFrag=SelectedMoviesOrSeriesFragment()
                val bundleObject= Bundle()

                bundleObject.putString("Poster",currrentData.poster_path)
                bundleObject.putInt("Movie_Id",currrentData.id)
                bundleObject.putString("Genre",holder.genreTextView.text.toString())
                bundleObject.putString("Overview",currrentData.overview)
                bundleObject.putString("MediaType",currrentData.media_type)
                nextFrag.arguments=bundleObject

                fragTransaction.replace( R.id.serach_activity_layout_for_frag,nextFrag)
                fragTransaction.addToBackStack(null)

                fragTransaction.commit()


                val actvityObject= (context as ActivityForDisplayingSearchResults)


                actvityObject.numberOfFragInBackStack++

                actvityObject.lifecycleScope.launch {

                    actvityObject.hasAnItemBeingSelected.emit(true)

                }

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

            try{
//                this code can throw a null pointer exception if genre_ids is null
                for (id in data.genre_ids){
                    for (genre in TheViewModel.genreForSeries?.genres!!){

                        if (id==genre.id){
                            holder.genreTextView.append("|${genre.name}")
                            break
                        }
                    }

                }
            }
            catch (e:NullPointerException){

                holder.genreTextView.append("|Unknown")

            }

        }







    }

}