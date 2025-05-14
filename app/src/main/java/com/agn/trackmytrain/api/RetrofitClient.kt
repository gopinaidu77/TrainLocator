package com.agn.trackmytrain.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://indian-railway-irctc.p.rapidapi.com/") // ✅ Use correct base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: TrainApiService = retrofit.create(TrainApiService::class.java)
}
