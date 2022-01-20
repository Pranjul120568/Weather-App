package com.pdinc.weather.models

data class Tempratures(
        val temp:Float,
        val temp_min:Float,
        val temp_max:Float,
        val pressure:Int,
        val humidity:Int,
        val sea_level:Int,
        val grnd_level:Int,
)
