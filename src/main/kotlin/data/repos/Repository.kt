package data.repos

import androidx.compose.runtime.mutableStateOf
import data.dao.WeatherDao_Impl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import utils.RequestState
import models.WeatherResult
import utils.WeatherTransformer
import utils.API_KEY

class Repository {
    private val dao = WeatherDao_Impl(apiKey = API_KEY)
    private val transformer = WeatherTransformer()

    // Publisher pattern
    private val _weather = MutableStateFlow<RequestState<WeatherResult>>(RequestState.Idle)
    val weather: StateFlow<RequestState<WeatherResult>> = _weather

    // Theme
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val queriedCity = mutableStateOf("")

    suspend fun weatherForCity() {
        _weather.value = RequestState.Loading
        try {
            val result = dao.getWeatherByCity(queriedCity.value)
            val data = transformer.transform(result)
            _weather.value = RequestState.Content(data)
        } catch (e: Exception) {
            println(e.message)
            _weather.value = RequestState.Error(e)
        }
    }

    fun updateQueriedCity(city: String) {
        queriedCity.value = city
    }

    fun changeTheme(bool: Boolean) {
        _isDarkTheme.value = bool
    }
}