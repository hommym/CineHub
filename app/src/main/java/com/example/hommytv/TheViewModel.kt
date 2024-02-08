package com.example.hommytv

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.hommytv.networkrequest.RetrofitObject
import com.example.hommytv.retrofitdataclasses.ContentFromServer
import com.example.hommytv.retrofitdataclasses.ContentType
import com.example.hommytv.retrofitdataclasses.MovieDetails
import com.example.hommytv.retrofitdataclasses.MoviesList
import com.example.hommytv.roomdatabase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.nio.file.Files
import kotlin.io.path.exists

class TheViewModel(): ViewModel() {

    var context:Context?=null
    var applicationContext:Application?=null


    fun dataInFavTable():LiveData<List<FavTable>>{
        return (applicationContext as App).repositoryObject.showFav().asLiveData()
    }

    suspend fun addToFav(data:FavTable){

        (applicationContext as App).repositoryObject.addToFav(data)
    }

    suspend fun removeFromFav(data: FavTable){
        (applicationContext as App).repositoryObject.removeFromFav(data)
    }


    fun dataInWatchLaterTable():LiveData<List<WatchLaterTable>>{
        return (applicationContext as App).repositoryObject.showWatchLater().asLiveData()
    }

    suspend fun addToWatchLater(data:WatchLaterTable){

        (applicationContext as App).repositoryObject.addToWatchLater(data)
    }

    suspend fun removeFromWatchLater(data: WatchLaterTable){
        (applicationContext as App).repositoryObject.removeFromWatchLater(data)
    }


    suspend fun addToHistory(data:HistoryTable){

        (applicationContext as App).repositoryObject.addToHistory(data)
    }

    fun showHistory():LiveData<List<HistoryTable>> {
        return  (applicationContext as App).repositoryObject.showHistory().asLiveData()
    }

    suspend fun removeFromHistory(data:HistoryTable){
        (applicationContext as App).repositoryObject.removeFromHistory(data)
    }

    suspend fun addToPlayListName(data:PlayListNameTable){
        (applicationContext as App).repositoryObject.addToPlaylistName(data)
    }

    fun showPlayListName():LiveData<List<PlayListNameTable>>{
        return    (applicationContext as App).repositoryObject.showPlaylistNames().asLiveData()
    }


    suspend fun addToPlayListItem(data:PlayListItemTable){
        (applicationContext as App).repositoryObject.addToPlaylistItem(data)
    }

    fun showPlayListItem():LiveData<List<PlayListItemTable>>{
        return    (applicationContext as App).repositoryObject.showPlaylistItems().asLiveData()
    }




    //    currentFragmentSectionsLayout and currentFragmentMainLayout holds refernce to the current activity
  val currentFragmentSectionsLayout:Fragment?
  get() = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.layout_for_sections)

    val currentFragmentMainLayout:Fragment?
        get() = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.frag_layout_main_activity)


//    _hasLoIn is variable for checking if users has succesfully logged in
   private val _hasLoggedIn=MutableLiveData<Boolean>(false)

    val hasLoggedIn: LiveData<Boolean>
    get() = _hasLoggedIn

    //    _hasLogOut is variable for checking if users has succesfully loggedOut
    private val _hasLoggedOut=MutableLiveData<Boolean>(false)

    val hasLoggedOut: LiveData<Boolean>
        get() = _hasLoggedOut

    fun logOut(){

        _hasLoggedOut.value=true
    }

// the purpose of this variable is to know when the current fragment is the profile fragmnet so that
//so  that i could make the buttom nav bar disappear if profile fragment comes up.

 val isCurrentFragmentProfile= MutableSharedFlow<Boolean>()

// currentFragmentIsProfile is just used for setting the value of _isCurrentFragmentProfile
    fun setFragmentIsProfileValue(value:Boolean){

        viewModelScope.launch {
            isCurrentFragmentProfile.emit(value)
        }

    }


    fun logIntoAcount(email:String,password:String){

//        codes for performing log in action (if is successful _hasLogIn is set to true)


//        i am setting _hasLogIn to true for testing purpposes
        _hasLoggedIn.value=true

    }



//    all the value for _hasNetworkRequestForHomeFinished is set
//    true when the last network request for home tab has finished
//private val _hasNetworkRequestForHomeFinished=MutableLiveData<Boolean>(false)

    val hasNetworkRequestFinished= MutableSharedFlow<Boolean>()




//    for network request failure

//    private val _hasNetworkRequestFailed=MutableLiveData<Boolean>(false)

    val hasNetworkRequestFailed= MutableSharedFlow<Boolean>()
     val coroutineErrorHandler= CoroutineExceptionHandler{context,error,->

         viewModelScope.launch {
             hasNetworkRequestFailed.emit(true)
         }



     }







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





    @RequiresApi(Build.VERSION_CODES.O)
    fun gettingAllDataForHomeTabFromServer(){

        var errorHolder:Throwable? =null


        val sharedMutex=Mutex()




        viewModelScope.launch(coroutineErrorHandler+Dispatchers.IO) {

//            //    creating a folder called ImagesForSlider for the app internal storage
//            val pathObjOfImagesForSlider= context!!.filesDir.toPath().resolve("ImagesForSlider")
//            if(!pathObjOfImagesForSlider.exists()){
//                Files.createDirectory(pathObjOfImagesForSlider)
//                Log.d("FileCreated","yes")
//            }




               coroutineScope {


                       Log.d("CoroutineError","Caught")
//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer
                       if(returnedImages.size==0){
//            network request for images and recent movies(with in the requestForImagesForMovies callback)
                           val requestForImagesForMovies= RetrofitObject.networkRequestMethods.getImages("movie")
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

////                                               creating image file in images for slider folder
//                                               val imgFilePath=pathObjOfImagesForSlider.resolve("${item.title}.jpg")
//
//                                               if(!imgFilePath.exists()){
//                                                   viewModelScope.launch(Dispatchers.IO) {
//
//
////                                                   creating an input stream from the url object created from the image string url in imageToAdd
//                                                       val  imageUrlObj=URL(imageToAdd)
//
//                                                       imageUrlObj.openStream().use {urlInputStream->
//
//                                                           urlInputStream.copyTo(Files.newOutputStream(imgFilePath))
//
//                                                       }
//
//
//                                                   }
//                                               }




//
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
                                   viewModelScope.launch {
                                   sharedMutex.withLock {

                                       errorHolder=t
                                   }

                                   }
                               }

                           })
                       }



//







//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer
                       if(returnedImages.size!=20){

//            requestForImagesForSeries only adds Series poster's url to returnedImages
                           val  requestForImagesForSeries=RetrofitObject.networkRequestMethods.getImages("tv")
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
                                   viewModelScope.launch {
                                       sharedMutex.withLock {

                                           errorHolder=t
                                       }

                                   }
                               }


                           })
                       }


//









//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer
                       if(returnedCurrentlyAiringSeries==null){
//                                        request for currently airing tv series
                           val requestForSeriesAiring=RetrofitObject.networkRequestMethods.getContent("tv","on_the_air")
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
                                   viewModelScope.launch {
                                       sharedMutex.withLock {

                                           errorHolder=t
                                       }

                                   }
                               }


                           })
                       }



//





//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer

                       if(returnedUpcomingMovies==null){
                           //                            request fo currently upcoming movies
                           val requestForUpcomingMovies=  RetrofitObject.networkRequestMethods.getUpcomingContent("movie")
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

                                   viewModelScope.launch {
                                       sharedMutex.withLock {

                                           errorHolder=t
                                       }

                                   }
                               }

                           })

                       }





//





//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer
                       if(returnedTopRatedSeries==null){
                           //                    network request for top rated series
                           val requestForTopRatedSeries= RetrofitObject.networkRequestMethods.getContent("tv","top_rated")
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
                                   viewModelScope.launch {
                                       sharedMutex.withLock {

                                           errorHolder=t
                                       }

                                   }

                               }


                           })
                       }




//






//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer
                       if(returnedRecentMovies==null){
                           //                                 request for recent movies
                           val recentMovies=RetrofitObject.networkRequestMethods.getContent("movie","popular")
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
                                   viewModelScope.launch {
                                       sharedMutex.withLock {

                                           errorHolder=t
                                       }

                                   }

                               }


                           })
                       }



//





//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer
                       if(genreForMovie==null){
//                    making request for movie genre object in order to interpretate the genre ids
                           val genre=  RetrofitObject.networkRequestMethods.getGenre("movie")
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
                                   viewModelScope.launch {
                                       sharedMutex.withLock {

                                           errorHolder=t
                                       }

                                   }
                               }


                           })
                       }










//                     checking if this request had finished in a previous execution of the method
//                       gettingAllDataFromServer
                       if(genreForSeries==null){
                           //                    making request for series genre object in order to interpretate the genre ids
                           val genre=RetrofitObject.networkRequestMethods.getGenre("tv")
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
                                   viewModelScope.launch {
                                       sharedMutex.withLock {

                                           errorHolder=t
                                       }

                                   }
                               }


                           })
                       }




                   async(Dispatchers.IO) {
                       while( genreForSeries==null || genreForMovie==null || returnedRecentMovies==null
                           || returnedTopRatedSeries==null || returnedUpcomingMovies==null
                           || returnedCurrentlyAiringSeries==null || returnedImages.size<20 ){

                           if (errorHolder!=null){
                               throw errorHolder as Throwable

                           }
                           continue
                       }
                   }.await()

//


                   launch {


                       hasNetworkRequestFinished.emit(true)

                   }

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




     val movieInfo= MutableSharedFlow<MovieDetails?>()

//    the method for getting movie or tv details
    fun getMovieDetails(movieId:Int?,mediaType:String){


        val movieDetails=RetrofitObject.networkRequestMethods.getMovieDetails(movieId = movieId,mediaType=mediaType)

        movieDetails.enqueue(object :Callback<MovieDetails>{
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {

                if(response.isSuccessful){



                       val data=response.body()

//                   getting recommended movie or series
                       RetrofitObject.networkRequestMethods.getRecommendation(movieId = movieId,mediaType=mediaType)
                           .enqueue(object :Callback<ContentFromServer>{
                               override fun onResponse(
                                   call: Call<ContentFromServer>,
                                   response: Response<ContentFromServer>
                               ) {
                                   if(response.isSuccessful){

                                     data!!.recommendations= response.body()!!.results
       //                        changing the posterpath and backdroppath into actual urls
                                       for (item in  data.recommendations){

                                           item.backdrop_path="https://image.tmdb.org/t/p/original${item.backdrop_path}"
                                           item.poster_path="https://image.tmdb.org/t/p/original${item.poster_path}"


                                       }

                                       viewModelScope.launch {
                                           movieInfo.emit(data)
                                       }
                                   }
                               }

                               override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {
                                   Toast.makeText(context,"Could not get details",Toast.LENGTH_SHORT).show()
                               }

                           })





                }
                else{
                    Toast.makeText(context,"${response.code()}",Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Toast.makeText(context,"Could not get details",Toast.LENGTH_SHORT).show()
            }

        })


    }



    var selectedCategoryData = MutableSharedFlow<ArrayList<MoviesList>>()

//    function for getting data when a category is selected in the category tab
    fun getDataForSelectedCategory(mediaType:String,category:String){

    if(category=="none"){
        RetrofitObject.networkRequestMethods.getUpcomingContent(mediaType)
            .enqueue(object:Callback<ContentFromServer> {
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

                        viewModelScope.launch {

                            selectedCategoryData.emit(response.body()!!.results)
                        }


                    }


                }

                override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {

                    Toast.makeText(context,"request for recent movies failed",Toast.LENGTH_SHORT).show()

                    viewModelScope.launch {
                        hasNetworkRequestFailed.emit(true)
                    }

                }


            })
    }
    else{

        RetrofitObject.networkRequestMethods.getContent(mediaType,category)
        .enqueue(object:Callback<ContentFromServer> {
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

                    viewModelScope.launch {

                        selectedCategoryData.emit(response.body()!!.results)
                    }


                }


            }

            override fun onFailure(call: Call<ContentFromServer>, t: Throwable) {

                Toast.makeText(context,"request for recent movies failed",Toast.LENGTH_SHORT).show()

                viewModelScope.launch {
                    hasNetworkRequestFailed.emit(true)
                }

            }


        })

        }







    }
    }

