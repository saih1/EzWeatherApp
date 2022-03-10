package ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.MenuBarScope

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuBarScope.FileMenu(
    selectedTheme: Boolean,
    changeTheme: (Boolean) -> Unit
) {
    Menu("Settings", mnemonic = 'S') {
        Menu("Theme") {
            RadioButtonItem(
                "Light",
                mnemonic = 'L',
                onClick = { changeTheme(false) },
                selected = !selectedTheme
            )
            RadioButtonItem(
                "Dark",
                mnemonic = 'D',
                onClick = { changeTheme(true) },
                selected = selectedTheme
            )
        }
    }
}