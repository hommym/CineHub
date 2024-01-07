package com.example.hommytv

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hommytv.retrofitdataclasses.MoviesList
import com.example.hommytv.roomdatabase.FavTable
import com.example.hommytv.roomdatabase.WatchLaterTable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TheAdapter():RecyclerView.Adapter<TheAdapter.Holder>() {
   var context:Context?=null
   var data= arrayListOf<MoviesList>()
    var section="Recent"
    var dataInFavTable= listOf<FavTable>()
    var dataInWatchLaterTable= listOf<WatchLaterTable>()
    class Holder(itemView: View):RecyclerView.ViewHolder(itemView){

        val poster:ImageView= itemView.findViewById(R.id.image_poster)
        val movieTitle:TextView=itemView.findViewById(R.id.movie_title)
        val itemContainer:CardView=itemView.findViewById(R.id.view_click_for_details)
         val favCheckBox:CheckBox=itemView.findViewById(R.id.fav_chekbox)
        val watchLaterCheckBox:CheckBox=itemView.findViewById(R.id.watch_later_chekbox)
//        (Sometimes we use this textView for holding genre)
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
        val currentData= data[position]



        holder.apply {


//            removing listners on favcheckbox and watchlatercheckbox
            favCheckBox.setOnCheckedChangeListener(null)
            watchLaterCheckBox.setOnCheckedChangeListener(null)

            (context as MainActivity).lifecycleScope.launch {

                coroutineScope {
                    launch {
                       for(it in dataInFavTable){
                           if(it.contentTitle==currentData.title || it.contentTitle==currentData.original_name){
                               favCheckBox.isChecked = true
                               break
                           }
                           else if(dataInFavTable.lastIndex==(dataInFavTable.size-1)){
                               favCheckBox.isChecked = false
                           }
                       }
                    }

                    launch {



                        for(it in dataInWatchLaterTable){
                            if(it.contentTitle==currentData.title || it.contentTitle==currentData.original_name){
                                watchLaterCheckBox.isChecked = true
                                break
                            }
                            else if(dataInWatchLaterTable.lastIndex==(dataInWatchLaterTable.size-1)){
                                watchLaterCheckBox.isChecked = false
                            }
                        }



                    }


                }

            }

//            setting image resource
            val imgUri= currentData.poster_path.toUri().buildUpon().scheme("https").build()
            poster.load(imgUri){
                placeholder(R.drawable.baseline_image_24)

            }

//            setting mediatype
            if(currentData.original_name==null){

                currentData.media_type="movie"
            }
            else{
                currentData.media_type="tv"
            }


//            checking f adapter is being used in main activity


//                setting the title and release dates or genres
              when("movie"){
                 currentData.media_type ->{

                      settingGenre(holder,currentData,"Movie")
                      movieTitle.text=currentData.title

                  }

                  else->{

                      settingGenre(holder,currentData,"Series")
                      movieTitle.text=currentData.original_name
                  }


              }





            itemContainer.setOnClickListener {

//       changing fragment to details fragment(SelectedMovieOrSeriesFragment)

                if(context is MainActivity){
                    val fragTransaction= (context as MainActivity).supportFragmentManager.beginTransaction()
                    fragTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.slide_in,R.anim.slide_out)
                    val nextFrag=SelectedMoviesOrSeriesFragment()
                    val bundleObject=Bundle()

                    bundleObject.putString("Poster",currentData.poster_path)
                    bundleObject.putInt("Movie_Id",currentData.id)
                    bundleObject.putString("Genre",holder.releaseDate.text.toString())
                    bundleObject.putString("Overview",currentData.overview)
                    bundleObject.putString("MediaType",currentData.media_type)
                    nextFrag.arguments=bundleObject

                    fragTransaction.replace( R.id.layout_for_sections,nextFrag)
                    fragTransaction.addToBackStack(null)

                    fragTransaction.commit()
                }
                else{
                    //       changing fragment to details fragment(SelectedMovieOrSeriesFragment)
                    val fragTransaction= (context as ActivityForDisplayingSearchResults).supportFragmentManager.beginTransaction()
                    fragTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.slide_in,R.anim.slide_out)
                    val nextFrag=SelectedMoviesOrSeriesFragment()
                    val bundleObject= Bundle()

                    bundleObject.putString("Poster",currentData.poster_path)
                    bundleObject.putInt("Movie_Id",currentData.id)
                    bundleObject.putString("Genre",holder.releaseDate.text.toString())
                    bundleObject.putString("Overview",currentData.overview)
                    bundleObject.putString("MediaType",currentData.media_type)
                    nextFrag.arguments=bundleObject

                    fragTransaction.replace( R.id.serach_activity_layout_for_frag,nextFrag)
                    fragTransaction.addToBackStack(null)

                    fragTransaction.commit()

                    (context as ActivityForDisplayingSearchResults).numberOfFragInBackStack++



                }

            }


//            setting up listeners to favCheckbox and watchLaterCheckBox
          favCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

              if(isChecked){
                  (context as MainActivity).lifecycleScope.launch {
                        val dataToAddToTable=FavTable(movieTitle.text.toString(),currentData.poster_path,currentData.id,currentData.media_type)
                      (context as MainActivity).viewModel.addToFav(dataToAddToTable)
                  }

                  Toast.makeText(context,"Added To Favorite",Toast.LENGTH_SHORT).show()
              }
              else{

                  (context as MainActivity).lifecycleScope.launch{


                      dataInFavTable.forEach {
                          if (it.contentTitle==movieTitle.text.toString()){

                              (context as MainActivity).viewModel.removeFromFav(it)

                          }
                      }

                  }
                  Toast.makeText(context,"Removed From Favorite",Toast.LENGTH_SHORT).show()

              }



          }

          watchLaterCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

              if(isChecked){
                  (context as MainActivity).lifecycleScope.launch {
                      val dataToAddToTable=WatchLaterTable(movieTitle.text.toString(),currentData.poster_path,currentData.id,currentData.media_type)
                      (context as MainActivity).viewModel.addToWatchLater(dataToAddToTable)
                  }

                  Toast.makeText(context,"Added To Watch Later",Toast.LENGTH_SHORT).show()
              }
              else{

                  (context as MainActivity).lifecycleScope.launch{


                      dataInWatchLaterTable.forEach {
                          if (it.contentTitle==movieTitle.text.toString()){

                              (context as MainActivity).viewModel.removeFromWatchLater(it)

                          }
                      }

                  }
                  Toast.makeText(context,"Removed From Watch Later",Toast.LENGTH_SHORT).show()

              }

          }

        }


    }


     fun settingGenre(holder:Holder, data:MoviesList, genreType:String){
//        the default for realeaseDate textView
        holder.releaseDate.text="Genre:"

         if(data.genre_ids!=null){

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

}