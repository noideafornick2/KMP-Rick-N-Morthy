package bruno.kmp.presentation.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import bruno.kmp.presentation.core.composables.characters.CharactersList
import bruno.kmp.presentation.core.composables.common.ManagementResourceUiState
import bruno.kmp.presentation.character_detail.CharacterDetailScreen
import bruno.kmp.presentation.core.composables.characters.CharacterActionAppBar
import bruno.kmp.presentation.core.composables.characters.ChipRow
import kotlinx.coroutines.flow.collectLatest

class CharactersScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey
    private val chipList = listOf("All", "Alive", "Dead", "Unknown")
    private var chipSelected = ""

    @Composable
    override fun Content() {
        val charactersViewModel = getScreenModel<CharactersViewModel>()
        val state by charactersViewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(key1 = Unit) {
            charactersViewModel.effect.collectLatest { effect ->
                when (effect) {
                    is CharactersContract.Effect.NavigateToDetailCharacter ->
                        navigator.push(CharacterDetailScreen(effect.idCharacter))
                }
            }
        }

        Scaffold(
            topBar = {
                CharacterActionAppBar (
                    onSearchQueryChanged = {
                        charactersViewModel.setEvent(
                            CharactersContract.Event.OnSearchCharacter(it)
                        )
                    }
                )
            }
        ) { padding ->
            Column (
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                LazyRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(4) { label ->
                        ChipRow(
                            label = chipList[label],
                            isSelected = chipSelected == chipList[label],
                            onClick = {
                                chipSelected = chipList[label]
                                charactersViewModel.setEvent(
                                    CharactersContract.Event.OnFilterCharacters(chipSelected)
                                )
                            }
                        )
                    }
                }

                ManagementResourceUiState(
                    modifier = Modifier.fillMaxSize(),
                    resourceUiState = state.characters,
                    successView = { characters ->
                        CharactersList(
                            characters = characters,
                            onCharacterClick = { idCharacter ->
                                charactersViewModel.setEvent(
                                    CharactersContract.Event.OnCharacterClick(idCharacter)
                                )
                            },
                            isLoadingMore = state.isLoadingMore,
                            onLoadMore = {
                                charactersViewModel.setEvent(
                                    CharactersContract.Event.OnLoadNextPage
                                )
                            }
                        )
                    },
                    onTryAgain = {
                        charactersViewModel.setEvent(CharactersContract.Event.OnTryCheckAgainClick)
                    },
                    onCheckAgain = {
                        charactersViewModel.setEvent(CharactersContract.Event.OnTryCheckAgainClick)
                    }
                )
            }
        }
    }
}
