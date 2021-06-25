package com.pdinc.weather.network

import android.util.Log
import com.pdinc.weather.BuildConfig
import com.pdinc.weather.adapter.searchCityAdapter
import com.pdinc.weather.models.CurrentWeather
import com.pdinc.weather.models.FetchAll
import com.pdinc.weather.network.retrofit.Client
import com.pdinc.weather.network.retrofit.Service
import kotlinx.coroutines.*
import retrofit2.Response


class WeatherRemoteDataSourceImpl :WeatherRemoteDataSource {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val retrofitClient:Service= Client.retrofit_service
    override suspend fun getWeatherBySearch(query:String): Response<FetchAll> =
            withContext(ioDispatcher){
                return@withContext try {
                    val result=retrofitClient.getSpecificWeather(query,BuildConfig.API_KEY)
                    if(result.isSuccessful){
                        Log.d("Query Done","Data Fetched")
                        val networkWeather=result.body()
                        Response.success(networkWeather)
                    }else{
                        Log.d("Query Unsuccesful","Data Fetched")
                        Response.success(null)
                    }
                }catch(e:Exception){
                    Log.d("Query Unsuccesful","Data Not Fetched")
                    Response.success(null)
                }
            }
    override suspend fun getWeatherByGps(latitude: Int, longitude: Int): Response<FetchAll?>  =
            withContext(ioDispatcher){
                return@withContext try {
                    val result=retrofitClient.getWeatherByGps(latitude,longitude)
                    if(result.isSuccessful){
                        val networkWeather= result.body()
                        Log.d("Query Ki maa ki","Data Fetched")
                        Response.success(networkWeather)
                    }else{
                        Log.d("Query ki maa ki","Data Fetched")
                        Response.success(null)
                    }
                }catch (exception:Exception){
                    Log.d("Query ki maa ki",exception.toString())

                    Response.success(null)
                }
            }

    override suspend fun getCurrentWeatherBySearch(query: String): Response<CurrentWeather> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = retrofitClient.getCurrentWeatherByName(query, BuildConfig.API_KEY)
                if (result.isSuccessful) {
                    Log.d("Query Done","Data Fetched")
                    val networkWeather = result.body()
                    Response.success(networkWeather)

                } else {
                    Log.d("Query Unsuccesful",query)
                    Response.success(null)
                }
            } catch (exception: Exception) {

                Log.d("Query Unsuccesful", exception.toString())
                Response.success(null)
            }
        }

    override suspend fun getCurrentWeatherByGps(latitude: Double, longitude: Double): Response<CurrentWeather>  =
            withContext(ioDispatcher){
                return@withContext try {
                    val result=retrofitClient.getCurrentByGps(latitude,longitude)
                    if(result.isSuccessful){
                        val networkWeather=result.body()
                        Response.success(networkWeather)
                    }else{
                        Response.success(null)
                    }
                }catch (exception:Exception){
                    Response.success(null)
                }
            }
}
