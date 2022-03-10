package data.dao

import models.WeatherResponse

interface WeatherDao {
    suspend fun getWeatherByCity(city: String) : WeatherResponse
}