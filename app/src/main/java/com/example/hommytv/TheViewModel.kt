package com.example.hommytv

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hommytv.networkrequest.RetrofitObject
import com.example.hommytv.retrofitdataclasses.ContentFromServer
import com.example.hommytv.retrofitdataclasses.ContentType
import com.example.hommytv.retrofitdataclasses.MoviesList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TheViewModel(): ViewModel() {

    var context:Context?=null


//    currentFragmentSectionsLayout and currentFragmentMainLayout holds refernce to the current activity
  val currentFragmentSectionsLayout:Fragment?
  get() = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.layout_for_sections)

    val currentFragmentMainLayout:Fragment?
        get() = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.frag_layout_main_activity)


//    _hasLoIn is variable for checking if users has succesfully logged in
   private val _hasLoggedIn=MutableLiveData<Boolean>(false)

    val hasLoggedIn: LiveData<Boolean>
    get() = _hasLoggedIn


// the purpose of this variable is to know when the current fragment is the profile fragmnet so that
//so  that i could make the buttom nav bar disappear if profile fragment comes up.

private val _isCurrentFragmentProfile= MutableSharedFlow<Boolean>()
val isCurrentFragmentProfile:SharedFlow<Boolean>
get() = _isCurrentFragmentProfile

// currentFragmentIsProfile is just used for setting the value of _isCurrentFragmentProfile
    fun setFragmentIsProfileValue(value:Boolean){

        viewModelScope.launch {
            _isCurrentFragmentProfile.emit(value)
        }

    }


    fun logIntoAcount(email:String,password:String){

//        codes for performing log in action (if is successful _hasLogIn is set to true)


//        i am setting _hasLogIn to true for testing purpposes
        _hasLoggedIn.value=true

    }


//    all the value for _hasNetworkRequestForHomeFinished is set
//    true when the last network request for home tab has finished
private val _hasNetworkRequestForHomeFinished=MutableLiveData<Boolean>(false)

    val hasNetworkRequestFinished:LiveData<Boolean>
    get() = _hasNetworkRequestForHomeFinished

//    returnedRecentMovies holds the data returned when the request getRecentMovies was sent
   var  returnedRecentMovies:ContentFromServer?=null

//    returnedImages holds the data returned when the request getImages was sent
   var returnedImages:ArrayList<String> = arrayListOf()

//    returnedUpcomingMovies holds the data returned when the request getUpcomingMovies was sent
 var  returnedUpcomingMovies:ContentFromServer? =null


//    returnedTopRatedSeries holds the data returned when the request getTopRatedSeries was sent
var returnedTopRatedSeries:ContentFromServer? =null

//    returnedCurrentlyAiringSeries holds the data returned when the request getTopRatedSeries was sent
var returnedCurrentlyAiringSeries:ContentFromServer? =null




    companion object{
        //  genreForSeries and genreForMovies is for holding the the values of the genre id (is a key(id) value(genre name) pair)
        var genreForSeries:ContentType?=null
        var genreForMovie:ContentType?=null

    }

    fun gettingAllDataForHomeTabFromServer(){


        viewModelScope.launch {

            coroutineScope {

                launch(Dispatchers.IO) {
                    //            network request for images and recent movies(with in the requestForImagesForMovies callback)
                    val requestForImagesForMovies= RetrofitObject.networkRequestMethods.getImagesForMovies()
                    requestForImagesForMovies.enqueue(object :Callback<ContentFromServer> {
                        override fun onResponse(
                            call: Call<ContentFromServer>,
                            response: Response<ContentFromServer>
                        ) {
                            if(response.isSuccessful){

                                Log.d("One request finish","Done")
                                var numberIterations=0
                                for (item in response.body()?.results!!){

                                    val imageToAdd="https://image.tmdb.org/t/p/original${item.poster_path}"
                                    if (numberIterations==10){
                                        break
                                    }
                                    else if(imageToAdd in returnedImages){
                                        continue
                                    }

                                    else{
//                                        adding image url to list of images that is
                                        returnedImages.add(imageToAdd)
                                    }

                                    numberIterations++
                                }


                            }
                            else{
                                Toast.makeText(context,"${response.code()}",Toast.LENGTH_SHORT).show()
                            }


                        }

                        override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {
                            Toast.makeText(context,"request for images failed",Toast.LENGTH_SHORT).show()

                        }

                    })


                    while (true){

                        if (returnedImages.size==10){
                            break
                        }
                    }

                    returnedImages

                }
                launch(Dispatchers.IO) {
                    //            requestForImagesForSeries only adds Series poster's url to returnedImages
                    val  requestForImagesForSeries=RetrofitObject.networkRequestMethods.getImagesForSeries()
                    requestForImagesForSeries.enqueue(object :Callback<ContentFromServer> {
                        override fun onResponse(
                            call: Call<ContentFromServer>,
                            response: Response<ContentFromServer>
                        ) {

                            if(response.isSuccessful){
                                Log.d("One request finish","Done")
                                var numberIterations=0
                                for (item in response.body()?.results!!){

                                    if (numberIterations==10){
                                        break
                                    }
//                           adding image url to list of images that is returnedImages
                                    returnedImages.add("https://image.tmdb.org/t/p/original${item.poster_path}")
                                    numberIterations++
                                }

                            }
                            else{
                                Toast.makeText(context,"${response.code()}",Toast.LENGTH_SHORT).show()
                            }



                        }

                        override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {

                            Toast.makeText(context,"request for images failed",Toast.LENGTH_SHORT).show()
                        }


                    })

                    while (true){

                        if (returnedImages.size==20){
                            break
                        }
                    }









                    returnedImages
                }
                launch (Dispatchers.IO){

                    //                                        request for currently airing tv series
                    val requestForSeriesAiring=RetrofitObject.networkRequestMethods.getAiringTodaySeries()
                    requestForSeriesAiring.enqueue(object:Callback<ContentFromServer>{
                        override fun onResponse(
                            call: Call<ContentFromServer>,
                            response: Response<ContentFromServer>
                        ) {
                            if(response.isSuccessful){
                                Log.d("One request finish","Done")
//                        changing the posterpath and backdroppath into actual urls
                                for (item in  response.body()?.results!!){

                                    item.backdrop_path="https://image.tmdb.org/t/p/original${item.backdrop_path}"
                                    item.poster_path="https://image.tmdb.org/t/p/original${item.poster_path}"


                                }

                                returnedCurrentlyAiringSeries=response.body()



                            }
                        }

                        override fun onFailure(
                            call: Call<ContentFromServer>,
                            t: Throwable
                        ) {
                            Toast.makeText(context,"request for currently airing series failed",Toast.LENGTH_SHORT).show()
                        }


                    })


                    while (true){
                        if (returnedCurrentlyAiringSeries!=null){
                            break
                        }
                    }
                }
                launch(Dispatchers.IO) {

                    //                            request fo currently upcoming movies
                    val requestForUpcomingMovies=  RetrofitObject.networkRequestMethods.getUpcomingMovies()
                    requestForUpcomingMovies.enqueue(object :Callback<ContentFromServer>{
                        override fun onResponse(
                            call: Call<ContentFromServer>,
                            response: Response<ContentFromServer>
                        ) {

                            if (response.isSuccessful){

                                Log.d("One request finish","Done")
//                        changing the posterpath and backdroppath into actual urls
                                for (item in  response.body()?.results!!){

                                    item.backdrop_path="https://image.tmdb.org/t/p/original${item.backdrop_path}"
                                    item.poster_path="https://image.tmdb.org/t/p/original${item.poster_path}"


                                }

                                returnedUpcomingMovies=response.body()

                            }
                        }


                        override fun onFailure(
                            call: Call<ContentFromServer>,
                            t: Throwable
                        ) {
                            Toast.makeText(context,"request for upcoming movies failed",Toast.LENGTH_SHORT).show()
                        }

                    })


                    while (true){
                        if (returnedUpcomingMovies!=null){
                            break
                        }
                    }
                }
                launch(Dispatchers.IO) {
//                    network request for top rated series
                    val requestForTopRatedSeries= RetrofitObject.networkRequestMethods.getTopRatedSeries()
                    requestForTopRatedSeries.enqueue(object:Callback<ContentFromServer> {
                        override fun onResponse(
                            call: Call<ContentFromServer>,
                            response: Response<ContentFromServer>
                        ) {
                            if (response.isSuccessful) {


                                Log.d("One request finish", "Done")
//                        changing the posterpath and backdroppath into actual urls
                                for (item in response.body()?.results!!) {

                                    item.backdrop_path =
                                        "https://image.tmdb.org/t/p/original${item.backdrop_path}"
                                    item.poster_path =
                                        "https://image.tmdb.org/t/p/original${item.poster_path}"


                                }

                                returnedTopRatedSeries = response.body()


                            }
                        }

                        override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {

                            Toast.makeText(context,"request for top rated series failed",Toast.LENGTH_SHORT).show()

                        }


                    })

                    while (true){
                        if (returnedTopRatedSeries!=null){
                            break
                        }
                    }
                }
                launch(Dispatchers.IO) {
                    //                                 request for recent movies
                    val recentMovies=RetrofitObject.networkRequestMethods.getRecentMovies()
                    recentMovies.enqueue(object:Callback<ContentFromServer> {
                        override fun onResponse(
                            call: Call<ContentFromServer>,
                            response: Response<ContentFromServer>
                        ) {


                            if (response.isSuccessful){
                                Log.d("One request finish","Done")
//                        changing the posterpath and backdroppath into actual urls
                                for (item in  response.body()?.results!!){

                                    item.backdrop_path="https://image.tmdb.org/t/p/original${item.backdrop_path}"
                                    item.poster_path="https://image.tmdb.org/t/p/original${item.poster_path}"


                                }

                                returnedRecentMovies=response.body()
                            }


                        }

                        override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {

                            Toast.makeText(context,"request for recent movies failed",Toast.LENGTH_SHORT).show()

                        }


                    })

                    while (true){
                        if (returnedRecentMovies!=null){
                            break
                        }
                    }
                }
                launch(Dispatchers.IO){

//                    making request for movie genre object in order to interpretate the genre ids
                    val genre=  RetrofitObject.networkRequestMethods.getGenreForMovie()

                    genre.enqueue(object :Callback<ContentType>{
                            override fun onResponse(
                                call: Call<ContentType>,
                                response: Response<ContentType>
                            ) {

                                if (response.isSuccessful){
                                    genreForMovie=response.body()
                                }
                            }

                            override fun onFailure(call: Call<ContentType>, t: Throwable) {
                                Toast.makeText(context,"request for movie genre failed",Toast.LENGTH_SHORT).show()
                            }


                        })


                    while (true){
                        if (genreForMovie!=null){
                            break
                        }
                    }


                }
                launch(Dispatchers.IO) {


//                    making request for series genre object in order to interpretate the genre ids
                 val genre=RetrofitObject.networkRequestMethods.getGenreForSeries()
                    genre.enqueue(object :Callback<ContentType>{
                        override fun onResponse(
                            call: Call<ContentType>,
                            response: Response<ContentType>
                        ) {

                            if (response.isSuccessful){
                                genreForSeries=response.body()
                            }
                        }

                        override fun onFailure(call: Call<ContentType>, t: Throwable) {
                            Toast.makeText(context,"request for series genre failed",Toast.LENGTH_SHORT).show()
                        }


                    })



                    while (true){
                        if (genreForSeries!=null){
                            break
                        }
                    }

                }

                }





                withContext(Dispatchers.Main){

                    _hasNetworkRequestForHomeFinished.value=true
                }

            }


}


//   _hasSearchRequestFinished is to help us know when the network request for searches will finish
  private val _hasSearchRequestFinished=MutableLiveData<Boolean>(false)
   val hasSearchRequestFinished:LiveData<Boolean>
   get() = _hasSearchRequestFinished

//    searchResults holds the data returned from the request sent
 var searchResults:ArrayList<MoviesList> = arrayListOf()




//    the method to execute for searches
    fun gettingSearchResults(keyword:String){

        val search= RetrofitObject.networkRequestMethods.getSearchResults(keyword)

//     sending request to server
        search.enqueue(object :Callback<ContentFromServer>{
            override fun onResponse(
                call: Call<ContentFromServer>,
                response: Response<ContentFromServer>
            ) {
               if (response.isSuccessful){

                   //  changing the posterpath and backdroppath into actual urls
                   for (item in  response.body()?.results!!){

                       item.backdrop_path="https://image.tmdb.org/t/p/original${item.backdrop_path}"
                       item.poster_path="https://image.tmdb.org/t/p/original${item.poster_path}"


                   }


            //checking if any of the element's property of the result property  is null(not yet implemented)


                    // setting the required data to search results
                     searchResults= response.body()!!.results
                    _hasSearchRequestFinished.value=true


               }

                else{

                   Toast.makeText(context,"${response.code()}",Toast.LENGTH_SHORT).show()
               }



            }

            override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {
                Toast.makeText(context,"Could not search",Toast.LENGTH_SHORT).show()
            }


        })


    }


    }

