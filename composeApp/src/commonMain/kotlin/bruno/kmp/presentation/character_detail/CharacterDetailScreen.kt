package bruno.kmp.presentation.character_detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import bruno.kmp.presentation.core.composables.character_detail.CharacterDetail
import bruno.kmp.presentation.core.composables.character_detail.CharacterDetailActionBar
import bruno.kmp.presentation.core.composables.common.ManagementResourceUiState
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class CharacterDetailScreen(
    private val characterId: Int,
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val characterDetailViewModel =
            getScreenModel<CharacterDetailViewModel> { parametersOf(characterId) }

        val state by characterDetailViewModel.uiState.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(key1 = Unit) {
            characterDetailViewModel.effect.collectLatest { effect ->
                when (effect) {
                    CharacterDetailContract.Effect.BackNavigation -> navigator.pop()
                }
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                CharacterDetailActionBar(
                    character = state.character,
                    onBackPressed = { characterDetailViewModel.setEvent(CharacterDetailContract.Event.OnBackPressed) }
                )
            }
        ) { padding ->
            ManagementResourceUiState (
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                resourceUiState = state.character,
                successView = { character ->
                    CharacterDetail(character)
                },
                onTryAgain = { characterDetailViewModel.setEvent(CharacterDetailContract.Event.OnTryCheckAgainClick) },
                onCheckAgain = { characterDetailViewModel.setEvent(CharacterDetailContract.Event.OnTryCheckAgainClick) },
            )
        }
    }
}
