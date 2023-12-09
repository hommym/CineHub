package com.example.hommytv.retrofitdataclasses

data class MoviesList(
    val adult: Boolean,
    var backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    var media_type: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    var poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val original_name:String
)