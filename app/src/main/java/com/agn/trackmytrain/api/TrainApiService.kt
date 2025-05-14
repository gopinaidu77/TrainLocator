package com.agn.trackmytrain.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TrainApiService {

    @Headers(
        "x-rapid-api: rapid-api-database",
        "x-rapidapi-host: irctc1.p.rapidapi.com",
        "x-rapidapi-key: 8d469e57f5msh84375eb1cabafcep126397jsn1e002f490e2a" // 🔐 Replace with your real key in production
    )
    @GET("api/trains-search/v1/train/{trainNo}")
    fun getTrainDetails(
        @Path("trainNo") trainNo: String,
        @Query("isH5") isH5: Boolean = true,
        @Query("client") client: String = "web"
    ): Call<TrainStatusResponse>
    @GET("api/v1/liveTrainStatus")
    fun getTrainLocation(
        @Query("trainNo") trainNo: String,
        @Query("startDate") startDate: String = "20240513"  // Use today's date in YYYYMMDD format
    ): Call<TrainLocationResponse>

}
