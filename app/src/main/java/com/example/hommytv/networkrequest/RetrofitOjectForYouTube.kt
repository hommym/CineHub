package com.example.hommytv.networkrequest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitOjectForYouTube {



    val retrofitForYoutubeData= Retrofit.Builder()
        .baseUrl("https://youtube.googleapis.com/youtube/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //    connecting retrofit instance with youtube request methods
    val networkRequestMethodsForYouTube:YouTubeNetworkRequestMethods by lazy{

        retrofitForYoutubeData.create(YouTubeNetworkRequestMethods::class.java)
    }

}