// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import data.repos.Repository
import kotlinx.coroutines.launch
import theme.WeatherAppTheme
import ui.FileMenu
import ui.WeatherScreen

fun main() = application {
    val repo = Repository()
    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        width = 650.dp,
        height = 650.dp
    )

    Window(
        onCloseRequest = { exitApplication() },
        title = "EzWeatherApp",
        state = windowState,
        resizable = false,
    ) {
        val weather by repo.weather.collectAsState()
        val scope = rememberCoroutineScope()
        val isDarkTheme by repo.isDarkTheme.collectAsState()
        MenuBar {
            FileMenu(selectedTheme = isDarkTheme,
                changeTheme = repo::changeTheme
            )
        }
        WeatherAppTheme(isDarkTheme) {
            WeatherScreen(
                weather = weather,
                updateQueriedCity = { repo.updateQueriedCity(it) },
                onSearchClick = { scope.launch { repo.weatherForCity() } }
            )
        }
    }
}
