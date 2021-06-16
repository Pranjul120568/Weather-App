package com.pdinc.weather.models

import com.google.gson.annotations.SerializedName

data class FetchAll(
        @SerializedName("city")
        val fetchCity:City,
        @SerializedName("list")
        val fetchlist:List<WeatherForecast>
)
