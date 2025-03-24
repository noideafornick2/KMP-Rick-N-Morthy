package bruno.kmp.presentation.characters

import cafe.adriel.voyager.core.model.coroutineScope
import bruno.kmp.domain.usecases.characters.GetCharactersByStatusUseCase
import bruno.kmp.domain.usecases.characters.GetCharactersUseCase
import bruno.kmp.domain.usecases.characters.GetSearchedCharactersUseCase
import bruno.kmp.domain.model.HomeState
import bruno.kmp.domain.model.SearchParams
import bruno.kmp.domain.model.StatusParams
import bruno.kmp.presentation.core.base.BaseViewModel
import bruno.kmp.presentation.model.ResourceUiState
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getSearchedCharactersUseCase: GetSearchedCharactersUseCase,
    private val getCharactersByStatusUseCase: GetCharactersByStatusUseCase
) : BaseViewModel<CharactersContract.Event, CharactersContract.State, CharactersContract.Effect>() {

    init {
        fetchCharacters(page = 1)
    }

    override fun createInitialState(): CharactersContract.State =
        CharactersContract.State(characters = ResourceUiState.Idle)

    override fun handleEvent(event: CharactersContract.Event) {
        when (event) {
            CharactersContract.Event.OnTryCheckAgainClick -> {
                setState { copy(currentPage = 1, hasReachedEnd = false) }
                fetchCharacters(page = 1, isRetry = true)
            }
            is CharactersContract.Event.OnCharacterClick -> setEffect {
                CharactersContract.Effect.NavigateToDetailCharacter(event.idCharacter)
            }
            CharactersContract.Event.OnLoadNextPage -> {
                val currentState = uiState.value
                if (!currentState.isLoadingMore && !currentState.hasReachedEnd) {
                    val nextPage = currentState.currentPage + 1
                    fetchCharacters(page = nextPage)
                }
            }
            is CharactersContract.Event.OnFilterCharacters -> {
                val status = event.status.lowercase().takeIf { it != "all" }
                setState {
                    copy(
                        currentPage = 1,
                        hasReachedEnd = false,
                        screenState = if (status == null) HomeState.DEFAULT else HomeState.FILTERED,
                        selectedFilter = status,
                        searchQuery = null
                    )
                }
                fetchCharacters(page = 1, isRetry = true)
            }
            is CharactersContract.Event.OnSearchCharacter -> {
                val name = event.name.trim().ifEmpty { null }
                setState {
                    copy(
                        currentPage = 1,
                        hasReachedEnd = false,
                        screenState = if (name == null) HomeState.DEFAULT else HomeState.SEARCHING,
                        searchQuery = name,
                        selectedFilter = null
                    )
                }
                fetchCharacters(page = 1, isRetry = true)
            }
        }
    }

    private fun fetchCharacters(page: Int, isRetry: Boolean = false) {
        if (page == 1 && !isRetry) {
            setState { copy(characters = ResourceUiState.Loading) }
        } else {
            setState { copy(isLoadingMore = true) }
        }

        val currentState = uiState.value

        coroutineScope.launch {
            when (currentState.screenState) {
                HomeState.DEFAULT -> getCharactersUseCase(page.toString())
                HomeState.SEARCHING -> getSearchedCharactersUseCase(
                    SearchParams(page.toString(), currentState.searchQuery ?: "")
                )
                HomeState.FILTERED -> getCharactersByStatusUseCase(
                    StatusParams(page.toString(), currentState.selectedFilter ?: "")
                )
            }.onSuccess { newCharacters ->
                    val currentList = if (page == 1 || isRetry) {
                        emptyList()
                    } else {
                        (uiState.value.characters as? ResourceUiState.Success)?.data ?: emptyList()
                    }
                    val updatedList = currentList + newCharacters
                    setState {
                        copy(
                            characters = if (updatedList.isEmpty()) ResourceUiState.Empty
                            else ResourceUiState.Success(updatedList),
                            isLoadingMore = false,
                            currentPage = page,
                            hasReachedEnd = newCharacters.isEmpty()
                        )
                    }
                }
                .onFailure { error ->
                    setState {
                        copy(
                            characters = if (page == 1) ResourceUiState.Error(error.message)
                            else uiState.value.characters,
                            isLoadingMore = false
                        )
                    }
                }
        }
    }
}
