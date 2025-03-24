package bruno.kmp.data

import bruno.kmp.data.mocks.MockCacheData
import bruno.kmp.data.mocks.MockRemoteData
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    private lateinit var repository: RepositoryImp
    private lateinit var mockCacheData: MockCacheData
    private lateinit var mockRemoteData: MockRemoteData
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockCacheData = MockCacheData()
        mockRemoteData = MockRemoteData()
        repository = RepositoryImp(mockCacheData, mockRemoteData)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacters returns characters from remote data`() = testScope.runTest {
        // Arrange
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species =  "",
                gender =  Gender.MALE,
                type =  "",
                origin =  "",
                location =  "",
                image =  "",
                page = "1"
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = Status.ALIVE,
                species =  "",
                gender =  Gender.MALE,
                type =  "",
                origin =  "",
                location =  "",
                image =  "",
                page = "1"
            )
        )
        mockRemoteData.characters = expectedCharacters
        val page = "1"

        // Act
        val result = repository.getCharacters(page)

        // Assert
        assertEquals(expectedCharacters, result)
    }

    @Test
    fun `getSearchedCharacters returns characters from remote data`() = testScope.runTest {
        // Arrange
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species =  "",
                gender =  Gender.MALE,
                type =  "",
                origin =  "",
                location =  "",
                image =  "",
                page = "1"
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = Status.ALIVE,
                species =  "",
                gender =  Gender.MALE,
                type =  "",
                origin =  "",
                location =  "",
                image =  "",
                page = "1"
            )
        )
        mockRemoteData.characters = expectedCharacters
        val page = "1"
        val name = "rick"

        // Act
        val result = repository.getSearchedCharacters(page, name)

        // Assert
        assertEquals(expectedCharacters, result)
    }

    @Test
    fun `getSearchedCharacters throws exception when remote data fails`() = testScope.runTest {
        // Arrange
        val exception = Exception("Network error")
        mockRemoteData.shouldThrow = exception
        val page = "1"
        val name = "rick"

        // Act & Assert
        assertFailsWith<Exception> {
            repository.getSearchedCharacters(page, name)
        }.also { thrown ->
            assertEquals(exception, thrown)
        }
    }

    @Test
    fun `getCharactersByStatus returns characters from remote data`() = testScope.runTest {
        // Arrange
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species =  "",
                gender =  Gender.MALE,
                type =  "",
                origin =  "",
                location =  "",
                image =  "",
                page = "1"
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = Status.ALIVE,
                species =  "",
                gender =  Gender.MALE,
                type =  "",
                origin =  "",
                location =  "",
                image =  "",
                page = "1"
            )
        )
        mockRemoteData.characters = expectedCharacters
        val page = "1"
        val status = "alive"

        // Act
        val result = repository.getCharactersByStatus(page, status)

        // Assert
        assertEquals(expectedCharacters, result)
    }

    @Test
    fun `getCharactersByStatus throws exception when remote data fails`() = testScope.runTest {
        // Arrange
        val exception = Exception("Network error")
        mockRemoteData.shouldThrow = exception
        val page = "1"
        val status = "alive"

        // Act & Assert
        assertFailsWith<Exception> {
            repository.getCharactersByStatus(page, status)
        }.also { thrown ->
            assertEquals(exception, thrown)
        }
    }

    @Test
    fun `getCharacter returns character from remote data`() = testScope.runTest {
        // Arrange
        val expectedCharacter = Character(
            id = 1,
            name = "Rick Sanchez",
            status = Status.ALIVE,
            species =  "",
            gender =  Gender.MALE,
            type =  "",
            origin =  "",
            location =  "",
            image =  "",
            page = "1"
        )
        mockRemoteData.singleCharacter = expectedCharacter
        val id = 1

        // Act
        val result = repository.getCharacterDetail(id)

        // Assert
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `getCharacter throws exception when remote data fails`() = testScope.runTest {
        // Arrange
        val exception = Exception("Network error")
        mockRemoteData.shouldThrow = exception
        val id = 1

        // Act & Assert
        assertFailsWith<Exception> {
            repository.getCharacterDetail(id)
        }.also { thrown ->
            assertEquals(exception, thrown)
        }
    }
}