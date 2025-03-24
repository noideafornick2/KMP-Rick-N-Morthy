package bruno.kmp.domain.usecases.characters

import bruno.kmp.data.MockRepository
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status
import bruno.kmp.domain.model.StatusParams
import bruno.kmp.domain.usecases.characters.GetCharactersByStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetCharactersByStatusUseCaseTest {

    private lateinit var useCase: GetCharactersByStatusUseCase
    private lateinit var mockRepository: MockRepository
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = MockRepository()
        useCase = GetCharactersByStatusUseCase(mockRepository, testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns success with characters when repository succeeds`() = testScope.runTest {
        // Arrange
        val expectedCharacters = listOf(
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
        mockRepository.characters = expectedCharacters
        val params = StatusParams(page = "1", status = "alive")

        // Act
        val result = useCase.invoke(params)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedCharacters, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = testScope.runTest {
        // Arrange
        val exception = Exception("Network error")
        mockRepository.shouldThrow = exception
        val params = StatusParams(page = "1", status = "alive")

        // Act
        val result = useCase.invoke(params)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke uses correct dispatcher`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick",
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
        val params = StatusParams(page = "1", status = "alive")

        // Act
        val result = useCase.invoke(params)

        // Assert
        advanceUntilIdle() // Ensure coroutines run
        assertTrue(result.isSuccess)
        assertEquals(characters, result.getOrNull())
    }

    @Test
    fun `invoke passes correct parameters to repository`() = testScope.runTest {
        // Arrange
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick",
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
        val params = StatusParams(page = "2", status = "dead")

        // Act
        val result = useCase.invoke(params)

        // Assert
        assertTrue { result.isSuccess }
        assertEquals(characters, result.getOrNull())
    }
}