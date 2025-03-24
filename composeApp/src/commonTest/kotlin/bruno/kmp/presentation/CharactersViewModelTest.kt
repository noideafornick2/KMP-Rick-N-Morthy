package bruno.kmp.presentation

import bruno.kmp.data.MockRepository
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.HomeState
import bruno.kmp.domain.model.Status
import bruno.kmp.domain.usecases.characters.GetCharactersByStatusUseCase
import bruno.kmp.domain.usecases.characters.GetCharactersUseCase
import bruno.kmp.domain.usecases.characters.GetSearchedCharactersUseCase
import bruno.kmp.presentation.characters.CharactersContract
import bruno.kmp.presentation.characters.CharactersViewModel
import bruno.kmp.presentation.model.ResourceUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class CharactersViewModelTest {

    private lateinit var viewModel: CharactersViewModel
    private lateinit var mockRepository: MockRepository
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var getSearchedCharactersUseCase: GetSearchedCharactersUseCase
    private lateinit var getCharactersByStatusUseCase: GetCharactersByStatusUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = MockRepository()
        getCharactersUseCase = GetCharactersUseCase(mockRepository, testDispatcher)
        getSearchedCharactersUseCase = GetSearchedCharactersUseCase(mockRepository, testDispatcher)
        getCharactersByStatusUseCase = GetCharactersByStatusUseCase(mockRepository, testDispatcher)
        viewModel = CharactersViewModel(
            getCharactersUseCase,
            getSearchedCharactersUseCase,
            getCharactersByStatusUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = testScope.runTest {
        // Assert
        val initialState = viewModel.uiState.value
        assertEquals(ResourceUiState.Loading, initialState.characters)
        assertEquals(HomeState.DEFAULT, initialState.screenState)
        assertEquals(1, initialState.currentPage)
        assertFalse(initialState.isLoadingMore)
        assertFalse(initialState.hasReachedEnd)
        assertNull(initialState.selectedFilter)
        assertNull(initialState.searchQuery)
    }

    @Test
    fun `init triggers fetchCharacters and sets loading state`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        mockRepository.characters = characters

        // Act
        advanceUntilIdle() // Let init run

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ResourceUiState.Success(characters), state.characters)
        assertEquals(1, state.currentPage)
        assertFalse(state.hasReachedEnd)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `fetchCharacters handles error on first page`() = testScope.runTest {
        // Arrange
        val exception = Exception("Network error")
        mockRepository.shouldThrow = exception

        // Act
        advanceUntilIdle() // Let init run

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ResourceUiState.Error("Network error"), state.characters)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `OnTryCheckAgainClick resets state and fetches characters`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        mockRepository.characters = characters
        viewModel.handleEvent(CharactersContract.Event.OnTryCheckAgainClick)

        // Act
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ResourceUiState.Success(characters), state.characters)
        assertEquals(1, state.currentPage)
        assertFalse(state.hasReachedEnd)
    }

    @Test
    fun `OnCharacterClick emits NavigateToDetailCharacter effect`() = testScope.runTest {
        // Arrange
        val id = 1
        val effects = mutableListOf<CharactersContract.Effect>()
        val job = launch {
            viewModel.effect.collect { effect ->
                effects.add(effect)
            }
        }

        // Act
        viewModel.handleEvent(CharactersContract.Event.OnCharacterClick(id))
        advanceUntilIdle()

        // Assert
        // Assert
        assertEquals<List<CharactersContract.Effect>>(
            listOf(CharactersContract.Effect.NavigateToDetailCharacter(id)),
            effects
        )

        // Clean up
        job.cancel()
    }

    @Test
    fun `OnFavoritesClick emits NavigateToFavorites effect`() = testScope.runTest {
        // Arrange
        val effects = mutableListOf<CharactersContract.Effect>()
        val job = launch {
            viewModel.effect.collect { effect ->
                effects.add(effect)
            }
        }

        // Act
        viewModel.handleEvent(CharactersContract.Event.OnFavoritesClick)
        advanceUntilIdle()

        // Assert
        assertEquals<List<CharactersContract.Effect>>(
            listOf(CharactersContract.Effect.NavigateToFavorites),
            effects
        )

        // Clean up
        job.cancel()
    }

    @Test
    fun `OnLoadNextPage fetches next page and appends characters`() = testScope.runTest {
        // Arrange
        val page1Characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        val page2Characters = listOf(
            Character(
                id = 2,
                name = "Morty Smith",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        mockRepository.characters = page1Characters
        advanceUntilIdle() // Load page 1

        // Update mock for page 2
        mockRepository.characters = page2Characters

        // Act
        viewModel.handleEvent(CharactersContract.Event.OnLoadNextPage)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        val expectedCharacters = page1Characters + page2Characters
        assertEquals(ResourceUiState.Success(expectedCharacters), state.characters)
        assertEquals(2, state.currentPage)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `OnFilterCharacters with status fetches filtered characters`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        mockRepository.characters = characters
        val status = "alive"

        // Act
        viewModel.handleEvent(CharactersContract.Event.OnFilterCharacters(status))
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ResourceUiState.Success(characters), state.characters)
        assertEquals(HomeState.FILTERED, state.screenState)
        assertEquals(status, state.selectedFilter)
        assertNull(state.searchQuery)
        assertEquals(1, state.currentPage)
        assertFalse(state.hasReachedEnd)
    }

    @Test
    fun `OnFilterCharacters with status all resets to default state`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        mockRepository.characters = characters

        // Act
        viewModel.handleEvent(CharactersContract.Event.OnFilterCharacters("all"))
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ResourceUiState.Success(characters), state.characters)
        assertEquals(HomeState.DEFAULT, state.screenState)
        assertNull(state.selectedFilter)
        assertNull(state.searchQuery)
        assertEquals(1, state.currentPage)
        assertFalse(state.hasReachedEnd)
    }

    @Test
    fun `OnSearchCharacter with name fetches searched characters`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        mockRepository.characters = characters
        val name = "rick"

        // Act
        viewModel.handleEvent(CharactersContract.Event.OnSearchCharacter(name))
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ResourceUiState.Success(characters), state.characters)
        assertEquals(HomeState.SEARCHING, state.screenState)
        assertEquals(name, state.searchQuery)
        assertNull(state.selectedFilter)
        assertEquals(1, state.currentPage)
        assertFalse(state.hasReachedEnd)
    }

    @Test
    fun `OnSearchCharacter with empty name resets to default state`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "",
                gender = Gender.MALE,
                type = "",
                origin = "",
                location = "",
                image = "",
                page = "1"
            )
        )
        mockRepository.characters = characters

        // Act
        viewModel.handleEvent(CharactersContract.Event.OnSearchCharacter(""))
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ResourceUiState.Success(characters), state.characters)
        assertEquals(HomeState.DEFAULT, state.screenState)
        assertNull(state.searchQuery)
        assertNull(state.selectedFilter)
        assertEquals(1, state.currentPage)
        assertFalse(state.hasReachedEnd)
    }
}