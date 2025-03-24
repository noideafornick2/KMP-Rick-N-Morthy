import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import bruno.kmp.App
import bruno.kmp.di.initKoin

fun main() = application {
    initKoin {}
    Window(
        title = "KMP Rick And Morty",
        state = rememberWindowState(width = 400.dp, height = 800.dp),
        onCloseRequest = ::exitApplication,
    ) { App() }
}