package com.pdinc.weather.network.retrofit

import com.pdinc.weather.models.CurrentWeather
import com.pdinc.weather.models.FetchAll
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    companion object {
        const val FORECAST="data/2.5/weather/forecast"
        const val WEATHERBYGPS="data/2.5/forecast"
        const val CURRENT="data/2.5/weather"
        const val CURRENTBYGPS="data/2.5/weather"
    }
@GET(FORECAST)
suspend fun getSpecificWeather(
        @Query("q") location:String,
        @Query("appid") apiKey:String
): Response<FetchAll>
@GET(WEATHERBYGPS)
suspend fun getWeatherByGps(
    @Query("lat")latitude:Double,
    @Query("lon")longitude:Double,
    @Query("appid") apiKey:String
): Response<FetchAll>
@GET(CURRENT)
suspend fun getCurrentWeatherByName(
        @Query("q") location: String,
        @Query("appid")apiKey: String
):Response<CurrentWeather>
@GET(CURRENTBYGPS)
    suspend fun getCurrentByGps(
            @Query("lat")latitude:Double,
            @Query("lon")longitude:Double,
            @Query("appid") apiKey:String
    ):Response<CurrentWeather>
}