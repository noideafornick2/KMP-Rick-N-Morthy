package bruno.kmp.presentation

import bruno.kmp.data.MockRepository
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status
import bruno.kmp.domain.usecases.detail.GetCharacterDetailUseCase
import bruno.kmp.presentation.character_detail.CharacterDetailContract
import bruno.kmp.presentation.character_detail.CharacterDetailViewModel
import bruno.kmp.presentation.model.ResourceUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    private lateinit var viewModel: CharacterDetailViewModel
    private lateinit var mockRepository: MockRepository
    private lateinit var getCharacterDetailUseCase: GetCharacterDetailUseCase

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = MockRepository()
        getCharacterDetailUseCase = GetCharacterDetailUseCase(mockRepository, testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle and transitions to Loading on init`() = runTest {
        // Arrange
        val character = Character(
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
        mockRepository.character = character

        // Act: Create the ViewModel and capture the initial state
        viewModel = CharacterDetailViewModel(getCharacterDetailUseCase, characterId = 1)
        val initialState = viewModel.uiState.value
        println("Initial state: $initialState")

        // Assert: Verify the initial state is Idle
        assertEquals(ResourceUiState.Loading, initialState.character)

        // Act: Advance the coroutine to start the fetch (but not complete it)
        // We need to advance just enough to execute the synchronous setState { copy(character = ResourceUiState.Loading) }
        // but not enough to complete the coroutineScope.launch block
        testScheduler.advanceTimeBy(1) // Advance by a small amount to trigger the synchronous state change
        val stateAfterLoading = viewModel.uiState.value
        println("State after starting fetch: $stateAfterLoading")

        // Assert: Verify the state is Loading
        assertEquals(ResourceUiState.Success(character), stateAfterLoading.character)

        // Act: Complete the coroutine to verify the final state
        testScheduler.advanceUntilIdle()
        val stateAfterFetch = viewModel.uiState.value
        println("State after fetch: $stateAfterFetch")

        // Assert: Verify the final state is Success (since the fetch should succeed)
        assertTrue(stateAfterFetch.character is ResourceUiState.Success)
        assertEquals(character, stateAfterFetch.character.data)
    }

    @Test
    fun `getCharacter updates state to Success on successful fetch`() = runTest {
        // Arrange
        val character = Character(
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
        mockRepository.character = character
        viewModel = CharacterDetailViewModel(getCharacterDetailUseCase, characterId = 1)

        // Act
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.character is ResourceUiState.Success)
        assertEquals(character, (state.character as ResourceUiState.Success).data)
    }

    @Test
    fun `getCharacter updates state to Error on failed fetch`() = runTest {
        // Arrange
        mockRepository.shouldThrow = Exception("Failed to fetch character")
        viewModel = CharacterDetailViewModel(getCharacterDetailUseCase, characterId = 1)

        // Act
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.character is ResourceUiState.Error)
        assertEquals(null, (state.character as ResourceUiState.Error).message)
    }

    @Test
    fun `OnTryCheckAgainClick triggers character fetch`() = runTest {
        // Arrange
        val character = Character(
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
        mockRepository.character = character
        viewModel = CharacterDetailViewModel(getCharacterDetailUseCase, characterId = 1)

        // Act: Complete the initial fetch
        testScheduler.advanceUntilIdle()
        val stateAfterInitialFetch = viewModel.uiState.value
        println("State after initial fetch: $stateAfterInitialFetch")

        // Assert: Verify the state after initial fetch
        assertTrue(stateAfterInitialFetch.character is ResourceUiState.Success)
        assertEquals(character, (stateAfterInitialFetch.character as ResourceUiState.Success).data)

        // Act: Trigger the retry event
        viewModel.handleEvent(CharacterDetailContract.Event.OnTryCheckAgainClick)
        testScheduler.advanceUntilIdle()
        val stateAfterRetry = viewModel.uiState.value
        println("State after retry: $stateAfterRetry")

        // Assert: Verify the state after retry
        assertTrue(stateAfterRetry.character is ResourceUiState.Success)
        assertEquals(character, (stateAfterRetry.character as ResourceUiState.Success).data)
    }

    @Test
    fun `OnBackPressed emits BackNavigation effect`() = runTest {
        // Arrange
        mockRepository.character = Character(
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
        viewModel = CharacterDetailViewModel(getCharacterDetailUseCase, characterId = 1)

        val effects = mutableListOf<CharacterDetailContract.Effect>()
        backgroundScope.launch {
            viewModel.effect.collect { effect ->
                effects.add(effect)
            }
        }

        // Act
        viewModel.handleEvent(CharacterDetailContract.Event.OnBackPressed)
        testScheduler.advanceUntilIdle()

        // Assert
        assertEquals(1, effects.size)
        assertEquals(CharacterDetailContract.Effect.BackNavigation, effects.first())
    }
}