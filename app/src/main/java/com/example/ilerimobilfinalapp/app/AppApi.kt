package com.example.ilerimobilfinalapp.app

import retrofit2.Response
import retrofit2.http.GET

interface AppApi {
    @GET("DisneyPosters.json")
    suspend fun getList(): Response<List<MovieEntity>>
}