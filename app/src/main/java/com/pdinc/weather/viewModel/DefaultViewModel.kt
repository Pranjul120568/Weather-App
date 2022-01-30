package com.pdinc.weather.viewModel

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import com.pdinc.weather.models.CurrentWeather
import com.pdinc.weather.models.FetchAll
import com.pdinc.weather.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class DefaultViewModel(): ViewModel() {

   var recyclerDetailsFromLatLonSearch:Response<FetchAll?>?=null
   var allWeatherDetails:Response<CurrentWeather>?= null
    var location1 :Location?=null
    private val weatherRemoteDataSourceImpl=WeatherRemoteDataSourceImpl()

  suspend fun getRecyclerViewDetails(): Response<FetchAll?> {
    recyclerDetailsFromLatLonSearch= weatherRemoteDataSourceImpl.getWeatherByGps(location1!!.latitude,location1!!.longitude)
    return recyclerDetailsFromLatLonSearch as Response<FetchAll?>
  }
    suspend fun getAllWeatherDetails():Response<CurrentWeather>{
        allWeatherDetails=weatherRemoteDataSourceImpl.getCurrentWeatherByGps(location1!!.latitude,location1!!.longitude)
        return allWeatherDetails as Response<CurrentWeather>
    }
    @SuppressLint("MissingPermission")
     fun startGPS() {
                runBlocking{
                    getRecyclerViewDetails()
                    getAllWeatherDetails()
            }
    }
}
