package data.dao

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.get
import models.*

class WeatherDao_Impl(
    private val apiKey: String
    ) : WeatherDao {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun getWeatherByCity(city: String): WeatherResponse {
        return client.get<WeatherResponse>(
            "https://api.weatherapi.com/v1/forecast.json" +
                    "?key=$apiKey" +
                    "&q=$city" +
                    "&days=5" +
                    "&aqi=no" +
                    "&alerts=no"
        )
    }
}