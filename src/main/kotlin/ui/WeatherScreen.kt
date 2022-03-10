package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextAlign
import models.WeatherCard
import models.WeatherResult
import utils.ImageDownloader
import utils.RequestState

@Composable
fun WeatherScreen(
    weather: RequestState<WeatherResult>,
    updateQueriedCity: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchBar(
                updateQueriedCity = updateQueriedCity,
                onSearchClick = onSearchClick
            )
            when (weather) {
                is RequestState.Loading -> LoadingUI()
                is RequestState.Error -> ErrorUI()
                is RequestState.Content -> ContentUI(weather.data)
                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    updateQueriedCity: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    var queriedCity by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 50.dp)
    ) {
        TextField(
            value = queriedCity,
            onValueChange = { queriedCity = it },
            placeholder = { Text("Any city, really...") },
            label = { Text(text = "Search for city") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.LocationOn, "Location") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        updateQueriedCity(queriedCity)
                        onSearchClick()
                    },
                    enabled = queriedCity.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                    )
                }
            },
            modifier = Modifier.onPreviewKeyEvent {
                if (it.key == Key.Enter
                    && it.type == KeyEventType.KeyDown
                    && queriedCity.isNotEmpty()
                ) {
                    updateQueriedCity(queriedCity)
                    onSearchClick()
                    true
                } else false
            }
        )
    }
}

@Composable
fun ContentUI(data: WeatherResult) {
    var imageState by remember { mutableStateOf<ImageBitmap?>(null) }
    val image: ImageBitmap? = imageState

    LaunchedEffect(key1 = data.currentWeather.iconUrl) {
        imageState = ImageDownloader.downloadImage(data.currentWeather.iconUrl)
    }
    Text(
        text = "Current weather in ${data.currentWeather.name}",
        modifier = Modifier.padding(all = 16.dp),
        style = MaterialTheme.typography.h6,
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 72.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = data.currentWeather.condition,
                style = MaterialTheme.typography.h6,
            )
            if (image != null) {
                Image(
                    bitmap = image,
                    contentDescription = "image",
                    modifier = Modifier
                        .defaultMinSize(minWidth = 128.dp, minHeight = 128.dp)
                        .padding(top = 8.dp)
                )
            }
            Text(
                text = "Temperature in °C: ${data.currentWeather.temperature}",
                modifier = Modifier.padding(all = 8.dp),
            )
            Text(
                text = "Feels like: ${data.currentWeather.feelsLike}",
                style = MaterialTheme.typography.caption,
            )
        }
    }
    Divider(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(all = 16.dp),
    )
    Text(
        text = "Forecast",
        modifier = Modifier.padding(all = 16.dp),
        style = MaterialTheme.typography.h6,
    )
    LazyRow {
        items(data.forecast) { weatherCard ->
            ForecastUI(weatherCard)
        }
    }
}

@Composable
fun ForecastUI(weatherCard: WeatherCard) {
    var imageState by remember { mutableStateOf<ImageBitmap?>(null) }
    val image: ImageBitmap? = imageState

    LaunchedEffect(key1 = weatherCard.iconUrl) {
        imageState = ImageDownloader.downloadImage(weatherCard.iconUrl)
    }
    Card(modifier = Modifier.padding(all = 4.dp)) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = weatherCard.condition,
                style = MaterialTheme.typography.h6
            )

            if (image != null) Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier
                    .defaultMinSize(minWidth = 64.dp, minHeight = 64.dp)
                    .padding(top = 8.dp)
            )

            val chanceOfRainText = String.format(
                "Chance of rain: %.2f%%", weatherCard.chanceOfRain
            )

            Text(
                text = chanceOfRainText,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}

@Composable
fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .defaultMinSize(minWidth = 96.dp, minHeight = 96.dp)
        )
    }
}

@Composable
fun ErrorUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Result not found, please try again!",
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 72.dp, vertical = 72.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.error,
        )
    }
}
