package com.pdinc.weather.network

import com.pdinc.weather.models.CurrentWeather
import com.pdinc.weather.models.FetchAll
import com.pdinc.weather.network.retrofit.Client
import com.pdinc.weather.network.retrofit.Service
import com.pdinc.weather.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeatherRemoteDataSourceImpl :WeatherRemoteDataSource {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val retrofitClient:Service= Client.retrofit_service
    override suspend fun getWeatherBySearch(query:String): Result<FetchAll> =
        withContext(ioDispatcher){
        return@withContext try {
            val result=retrofitClient.getSpecificWeather(query)
            if(result.isSuccessful){
                val networkWeather=result.body()
                Result.Success(networkWeather)
            }else{
              Result.Success(null)
            }
        }catch (exception:Exception){
              Result.Error(exception)
        }
        }

    override suspend fun getWeatherByGps(latitude: Double, longitude: Double): Result<FetchAll>  =
            withContext(ioDispatcher){
                return@withContext try {
                    val result=retrofitClient.getWeatherByGps(latitude,longitude)
                    if(result.isSuccessful){
                        val networkWeather=result.body()
                        Result.Success(networkWeather)
                    }else{
                        Result.Success(null)
                    }
                }catch (exception:Exception){
                    Result.Error(exception)
                }
            }

    override suspend fun getCurrentWeatherBySearch(query: String): Result<CurrentWeather> =
            withContext(ioDispatcher){
                return@withContext try {
                    val result=retrofitClient.getCurrentWeatherByName(query)
                    if(result.isSuccessful){
                        val networkWeather=result.body()
                        Result.Success(networkWeather)
                    }else{
                        Result.Success(null)
                    }
                }catch (exception:Exception){
                    Result.Error(exception)
                }
            }

    override suspend fun getCurrentWeatherByGps(latitude: Double, longitude: Double): Result<CurrentWeather>  =
            withContext(ioDispatcher){
                return@withContext try {
                    val result=retrofitClient.getCurrentByGps(latitude,longitude)
                    if(result.isSuccessful){
                        val networkWeather=result.body()
                        Result.Success(networkWeather)
                    }else{
                        Result.Success(null)
                    }
                }catch (exception:Exception){
                    Result.Error(exception)
                }
            }
}
