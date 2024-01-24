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
import com.example.hommytv.roomdatabase.HistoryTable
import kotlinx.coroutines.launch

class TheAdapterForListBasedRecyclerView (): RecyclerView.Adapter<TheAdapterForListBasedRecyclerView.Holder>() {
    var context: Context?=null
    var dataForSearch= arrayListOf<MoviesList>()
    var dataFromDatabase= listOf<DataHolder>()

    var adapterNotForSearchResult=false

//    variable to determine which type of list in you frag i will be using
      var   listTypeInYouFragment="History"

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







        if(adapterNotForSearchResult){
            val currentDataFromDatabase=dataFromDatabase[position]

            holder.apply {
                //            setting image resource
                val imgUri= currentDataFromDatabase.imgUrl.toUri().buildUpon().scheme("https").build()
                poster.load(imgUri){
                    placeholder(R.drawable.baseline_image_24)

                }


                //            setting title
                movieTitle.text=currentDataFromDatabase.contentTitle


//                   setting the media type(i would be using TextView for genre)
                   genreTextView.text=currentDataFromDatabase.mediaType


//            adding click listener to option menu
                optionMenu.setOnClickListener {

//   display context menu with add to favourite and watchlist as options(not yet implemented)

                }


                item.setOnClickListener {

                    if(listTypeInYouFragment!="History"){
                        //                    adding data to history table
                        val historyObj= HistoryTable(movieTitle.text.toString(),currentDataFromDatabase.imgUrl,currentDataFromDatabase.contentId,currentDataFromDatabase.mediaType)
                        (context as ActivityForDisplayingSearchResults).lifecycleScope.launch {
                            (context as ActivityForDisplayingSearchResults).viewModel.addToHistory(historyObj)
                        }
                    }

//       changing fragment to details fragment(SelectedMovieOrSeriesFragment)
                    val fragTransaction= (context as ActivityForDisplayingSearchResults).supportFragmentManager.beginTransaction()
                    fragTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.slide_in,R.anim.slide_out)
                    val nextFrag=SelectedMoviesOrSeriesFragment()
                    val bundleObject= Bundle()
                    bundleObject.putString("MediaType",currentDataFromDatabase.mediaType)
                    bundleObject.putString("Poster",currentDataFromDatabase.imgUrl)
                    bundleObject.putInt("Movie_Id",currentDataFromDatabase.contentId)
                    nextFrag.arguments=bundleObject

                    fragTransaction.replace(R.id.serach_activity_layout_for_frag,nextFrag)
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
        else{
            val currentData= dataForSearch[position]
            holder.apply {


                //            setting image resource
                val imgUri= currentData.poster_path.toUri().buildUpon().scheme("https").build()
                poster.load(imgUri){
                    placeholder(R.drawable.baseline_image_24)

                }

//            setting title
                when(currentData.media_type){

                    "movie"->{

                        movieTitle.text=currentData.title
                    }


                    else->{
                        movieTitle.text=currentData.original_name
                    }


                }




//            setting the genre
                settingGenre(holder,currentData)


//adding click listner to option icon

                optionMenu.setOnClickListener {


//   display context menu with add to favourite and watchlist as options(not yet implemented)



                }

//            setting listener for when an item is selected

                item.setOnClickListener {

                    //                    adding data to history table
                    val historyObj= HistoryTable(movieTitle.text.toString(),currentData.poster_path,currentData.id,currentData.media_type)
                    (context as ActivityForDisplayingSearchResults).lifecycleScope.launch {
                        (context as ActivityForDisplayingSearchResults).viewModel.addToHistory(historyObj)
                    }

                    //       changing fragment to details fragment(SelectedMovieOrSeriesFragment)
                    val fragTransaction= (context as ActivityForDisplayingSearchResults).supportFragmentManager.beginTransaction()
                    fragTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.slide_in,R.anim.slide_out)
                    val nextFrag=SelectedMoviesOrSeriesFragment()
                    val bundleObject= Bundle()

                    bundleObject.putString("Poster",currentData.poster_path)
                    bundleObject.putInt("Movie_Id",currentData.id)
                    bundleObject.putString("Genre",holder.genreTextView.text.toString())
                    bundleObject.putString("Overview",currentData.overview)
                    bundleObject.putString("MediaType",currentData.media_type)
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




    }


    override fun getItemCount(): Int {
        return if(adapterNotForSearchResult){
            dataFromDatabase.size
        }
        else{
            dataForSearch.size
        }
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