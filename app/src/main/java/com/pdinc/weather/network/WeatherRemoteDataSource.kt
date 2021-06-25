package com.pdinc.weather.network

import com.pdinc.weather.models.CurrentWeather
import com.pdinc.weather.models.FetchAll
import com.pdinc.weather.utils.Result
import retrofit2.Response

interface WeatherRemoteDataSource {
suspend fun getWeatherBySearch(query:String):Response<FetchAll>
suspend fun getWeatherByGps(latitude:Int,longitude:Int):Response<FetchAll?>
suspend fun getCurrentWeatherBySearch(query:String):Response<CurrentWeather>
suspend fun getCurrentWeatherByGps(latitude:Double,longitude:Double):Response<CurrentWeather>
}