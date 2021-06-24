package com.pdinc.weather.models

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
	val dt: Int? = null,
	val coord: Coord? = null,
	val visibility: Int? = null,
	val weather: List<WeatherItem?>? = null,
	val name: String? = null,
	val cod: Int? = null,
	val main: Main? = null,
	val clouds: Clouds? = null,
	val id: Int? = null,
	val sys: Sys? = null,
	val base: String? = null,
	@SerializedName("wind")
	val wind: wind1? = null
)

data class Clouds(
	val all: Int? = null
)

data class wind1(
	val deg: Int? = null,
	val speed: Double? = null
)

data class Sys(
	val country: String? = null,
	val sunrise: Int? = null,
	val sunset: Int? = null,
	val id: Int? = null,
	val type: Int? = null,
	val message: Double? = null
)

data class Main(
	val temp: Double? = null,
	val tempMin: Double? = null,
	val humidity: Int? = null,
	val pressure: Int? = null,
	val tempMax: Double? = null
)

data class WeatherItem(
	val icon: String? = null,
	val description: String? = null,
	val main: String? = null,
	val id: Int? = null
)

data class Coord(
	val lon: Double? = null,
	val lat: Double? = null
)

