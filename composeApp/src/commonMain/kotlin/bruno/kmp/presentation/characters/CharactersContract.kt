package bruno.kmp.presentation.characters

import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.HomeState
import bruno.kmp.presentation.model.ResourceUiState
import bruno.kmp.presentation.mvi.UiEffect
import bruno.kmp.presentation.mvi.UiEvent
import bruno.kmp.presentation.mvi.UiState

interface CharactersContract {
    sealed interface Event : UiEvent {
        data object OnTryCheckAgainClick : Event
        data object OnLoadNextPage : Event
        data class OnCharacterClick(val idCharacter: Int) : Event
        data class OnSearchCharacter(val name: String) : Event
        data class OnFilterCharacters(val status: String) : Event
    }

    data class State(
        val characters: ResourceUiState<List<Character>>,
        val isLoadingMore: Boolean = false,
        val currentPage: Int = 1,
        val hasReachedEnd: Boolean = false,
        val screenState: HomeState = HomeState.DEFAULT,
        val selectedFilter: String? = null,
        val searchQuery: String? = null
    ) : UiState

    sealed interface Effect : UiEffect {
        data class NavigateToDetailCharacter(val idCharacter: Int) : Effect
    }
}
