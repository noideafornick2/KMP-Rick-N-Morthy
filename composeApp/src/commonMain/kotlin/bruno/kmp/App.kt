package bruno.kmp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import bruno.kmp.presentation.core.theme.AppTheme
import bruno.kmp.presentation.characters.CharactersScreen

@Composable
internal fun App() = AppTheme {
    Navigator(CharactersScreen())
}

