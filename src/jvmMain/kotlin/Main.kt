import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import model.SharedViewModel
import ui.screens.LoginScreen
import utils.SettingManager

@Composable
@Preview
fun App() {
    MaterialTheme {
        LoginScreen(SharedViewModel())
    }
}

fun main() = application {
    SettingManager.init()
    Window(onCloseRequest = ::exitApplication,
        title = "花生酱的NAS",
        icon = painterResource("logo.png"),
        state = rememberWindowState(height = 900.dp, width = 450.dp)) {
        App()
    }
}
