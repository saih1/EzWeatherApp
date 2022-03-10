package models

data class WeatherCard(
    val name: String,
    val condition: String,
    val iconUrl: String,
    val temperature: Double,
    val feelsLike: Double,
    val chanceOfRain: Double? = null
)

data class WeatherResult(
    val currentWeather: WeatherCard,
    val forecast: List<WeatherCard>
)