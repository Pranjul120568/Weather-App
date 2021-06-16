package com.pdinc.weather.models

import com.google.gson.annotations.SerializedName

data class WeatherForecast (
        @SerializedName("dt_txt")
        val date:String,
        @SerializedName("dt")
        val dateunrefined:Long,
        @SerializedName("wind")
        val winddata:wind,
        @SerializedName("weather")
        val description:List<weatherDescription>,
        @SerializedName("main")
        val weatherTemp:List<Tempratures>
        )