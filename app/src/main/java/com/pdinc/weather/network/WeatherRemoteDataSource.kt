package com.pdinc.weather.network

import com.pdinc.weather.models.FetchAll
import com.pdinc.weather.utils.Result

interface WeatherRemoteDataSource {
suspend fun getWeatherBySearch(query:String):Result<FetchAll>
suspend fun getWeatherByGps(latitude:Double,longitude:Double):Result<FetchAll>
}