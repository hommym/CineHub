package com.example.hommytv.networkrequest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

//    creating retrofit instance
    val retrofit=Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()



//    connecting retrofit instance with methods
    val networkRequestMethods:NetworkRequestMethods by lazy {
        retrofit.create(NetworkRequestMethods::class.java)
    }



}