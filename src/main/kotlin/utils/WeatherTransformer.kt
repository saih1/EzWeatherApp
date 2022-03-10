package utils

import models.WeatherCard
import models.WeatherResponse
import models.WeatherResult

class WeatherTransformer {
    private fun extractCurrentWeatherForm(response: WeatherResponse): WeatherCard {
        return WeatherCard(
            name = "${response.location.name}, ${response.location.region}, ${response.location.country}",
            condition = response.current.condition.text,
            iconUrl = "https:" + response.current.condition.icon.replace("64x64", "128x128"),
            temperature = response.current.tempC,
            feelsLike = response.current.feelslikeC
        )
    }

    private fun extractForecastWeatherFrom(response: WeatherResponse): List<WeatherCard> {
        return response.forecast.forecastday.map { forecastday ->
            WeatherCard(
                name = "${response.location.name}, ${response.location.region}, ${response.location.country}",
                condition = forecastday.day.condition.text,
                iconUrl = "https:" + forecastday.day.condition.icon,
                temperature = forecastday.day.avgtempC,
                feelsLike = forecastday.hour.map { it.feelslikeC }.average(),
                chanceOfRain = forecastday.hour.map { it.chanceOfRain }.average()
            )
        }
    }

    fun transform(response: WeatherResponse): WeatherResult {
        val current = extractCurrentWeatherForm(response)
        val forecast = extractForecastWeatherFrom(response)

        return WeatherResult(
            currentWeather = current,
            forecast = forecast
        )
    }
}