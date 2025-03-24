import androidx.compose.ui.window.ComposeUIViewController
import bruno.avila.rnm.kmm.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}
