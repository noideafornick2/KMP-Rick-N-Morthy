package bruno.kmp.presentation.character_detail

import bruno.kmp.domain.model.Character
import bruno.kmp.presentation.model.ResourceUiState
import bruno.kmp.presentation.mvi.UiEffect
import bruno.kmp.presentation.mvi.UiEvent
import bruno.kmp.presentation.mvi.UiState

interface CharacterDetailContract {
    sealed interface Event : UiEvent {
        object OnTryCheckAgainClick : Event
        object OnBackPressed : Event
    }

    data class State(
        val character: ResourceUiState<Character>
    ) : UiState

    sealed interface Effect : UiEffect {
        object BackNavigation : Effect
    }
}