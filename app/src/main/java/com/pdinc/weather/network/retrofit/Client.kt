package com.pdinc.weather.network.retrofit

import com.pdinc.weather.network.retrofit.EndPoint.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    val retrofitClient=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit_service by lazy {
        retrofitClient.create(Service::class.java)
    }
}