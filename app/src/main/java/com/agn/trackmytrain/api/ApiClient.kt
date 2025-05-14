package com.agn.trackmytrain.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://irctc1.p.rapidapi.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-rapidapi-host", "irctc1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "8d469e57f5msh84375eb1cabafcep126397jsn1e002f490e2a") // 🔐 Replace for prod
                .build()
            chain.proceed(request)
        })
        .build()

    val apiService: TrainApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrainApiService::class.java)
}
