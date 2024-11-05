package com.example.monapp

import com.example.monapp.models.MovieResponse
import com.example.monapp.models.ResultActors
import com.example.monapp.models.ResultsSeries
import com.example.monapp.models.SeriesModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    //movies
    @GET("trending/movie/week")
    suspend fun lastmovies(@Query("api_key") api_key: String): MovieResponse
    @GET("search/movie")
    suspend fun movieKeyWord(@Query("api_key")apikey: String,@Query("query") motcle: String) : MovieResponse

    //acteurs
    @GET("trending/person/week")
    suspend fun lastActors(@Query("api_key") apikey: String):ResultActors
    @GET("search/person")
    suspend fun actorsKeyWord(@Query("api_key")apikey: String,@Query("query") motcle: String) : ResultActors

    //series
    @GET("trending/tv/week")
    suspend fun lastSeries(@Query("api_key")apikey: String) : ResultsSeries
    @GET("search/tv")
    suspend fun seriesKeyWord(@Query("api_key")apikey: String,@Query("query") motcle: String) : ResultsSeries
    @GET ("tv/{id}")
    suspend fun serieInfo (@Path("id") id: String, @Query("api_key")apikey: String, @Query("append_to_response")append_to_response:String):SeriesModel
}