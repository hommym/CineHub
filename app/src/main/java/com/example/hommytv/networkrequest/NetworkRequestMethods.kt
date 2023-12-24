package com.example.hommytv.networkrequest

import com.example.hommytv.retrofitdataclasses.ContentFromServer
import com.example.hommytv.retrofitdataclasses.ContentType
import com.example.hommytv.retrofitdataclasses.MovieDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkRequestMethods {



   @GET("/3/{media_type}/{category}")
   fun getContent(@Path("media_type")mediaType:String
   ,@Path("category")category:String
   ,@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>

//   @GET("/3/movie/popular")
//   fun getRecentMovies(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>

   @GET("/3/discover/{media_type}")
   fun getUpcomingContent(@Path("media_type")mediaType:String,@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"
    ,@Query("primary_release_year")releaseYear:String="2024|2025"):Call<ContentFromServer>

//   @GET("/3/discover/movie")
//   fun getUpcomingMovies(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"
//    ,@Query("primary_release_year")releaseYear:String="2024|2025"):Call<ContentFromServer>


//   @GET("/3/tv/top_rated")
//   fun getTopRatedSeries(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>

//   @GET("/3/tv/on_the_air")
//   fun getAiringTodaySeries(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>

   @GET("/3/trending/{media_type}/week")
   fun getImages(  @Path("media_type")mediaType:String,@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>

//   @GET("/3/trending/movie/week")
//   fun getImagesForMovies(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>
//
//
//   @GET("/3/trending/tv/week")
//   fun getImagesForSeries(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>

   @GET("/3/search/multi")
   fun getSearchResults(@Query("query")keyword:String,
   @Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>

   @GET("/3/genre/{media_type}/list")
   fun getGenre(@Path("media_type")mediaType:String,@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentType>

//   @GET("/3/genre/tv/list")
//   fun getGenreForSeries(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentType>
//
//
//   @GET("/3/genre/movie/list")
//   fun getGenreForMovie(@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentType>


   @GET("/3/{media_type}/{movie_id}")
   fun getMovieDetails(@Path("movie_id")movieId:Int?,@Path("media_type")mediaType:String,@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<MovieDetails>

   @GET("/3/{media_type}/{movie_id}/similar")
   fun getRecommendation(@Path("movie_id")movieId:Int?,@Path("media_type")mediaType:String,@Query("api_key")apiKey:String="5df9d8434a271efeaf152516c002398d"):Call<ContentFromServer>




}