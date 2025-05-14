package com.agn.trackmytrain.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrainApiService {

    @GET("api/trains-search/v1/train/{trainNo}")
    fun getTrainDetails(
        @Path("trainNo") trainNo: String,
        @Query("isH5") isH5: Boolean = true,
        @Query("client") client: String = "web"
    ): Call<TrainStatusResponse>

    @GET("api/v1/liveTrainStatus")
    fun getTrainLocation(
        @Query("trainNo") trainNo: String,
        @Query("startDate") startDate: String
    ): Call<TrainLocationResponse>
}
