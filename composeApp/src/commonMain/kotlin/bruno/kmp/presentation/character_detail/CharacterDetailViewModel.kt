package bruno.kmp.presentation.character_detail

import cafe.adriel.voyager.core.model.coroutineScope
import bruno.kmp.domain.usecases.detail.GetCharacterDetailUseCase
import bruno.kmp.presentation.core.base.BaseViewModel
import bruno.kmp.presentation.model.ResourceUiState
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    private val characterId: Int,
) : BaseViewModel<
        CharacterDetailContract.Event,
        CharacterDetailContract.State,
        CharacterDetailContract.Effect
    >() {

    init {
        getCharacter(characterId)
    }

    override fun createInitialState(): CharacterDetailContract.State =
        CharacterDetailContract.State(
            character = ResourceUiState.Idle
        )

    override fun handleEvent(event: CharacterDetailContract.Event) {
        when (event) {
            CharacterDetailContract.Event.OnTryCheckAgainClick ->
                getCharacter(characterId)
            CharacterDetailContract.Event.OnBackPressed ->
                setEffect { CharacterDetailContract.Effect.BackNavigation }
        }
    }

    private fun getCharacter(characterId: Int) {
        setState { copy(character = ResourceUiState.Loading) }
        coroutineScope.launch {
            getCharacterDetailUseCase(characterId)
                .onSuccess { setState { copy(character = ResourceUiState.Success(it)) } }
                .onFailure { setState { copy(character = ResourceUiState.Error()) } }
        }
    }
}