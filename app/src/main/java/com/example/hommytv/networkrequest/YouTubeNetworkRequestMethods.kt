package com.example.hommytv.networkrequest

import com.example.hommytv.retrofitdataclasses.TrailerVideo
import com.example.hommytv.retrofitdataclasses.YouTubeSearchResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeNetworkRequestMethods {


@GET("search")
fun videoId(@Query("key")apiKey:String="AIzaSyAap1_9sWrhHsYMa-1RgXjvGIz4dsRnWMQ",
@Query("part")part:String="snippet",
@Query("q")searchKeyword:String,
@Query("type")contentType:String="video",
@Query("videoEmbeddable")videoEmbeddable:Boolean=true,
@Query("fields")fields:String="items(id(videoId))"
):Call<YouTubeSearchResults>


//    videos
@GET("videos")
fun video(@Query("key")apiKey:String="AIzaSyAap1_9sWrhHsYMa-1RgXjvGIz4dsRnWMQ",
            @Query("part")part:String="player",
            @Query("fields")fields:String="items(player)",
            @Query("id")id:String
):Call<TrailerVideo>

}