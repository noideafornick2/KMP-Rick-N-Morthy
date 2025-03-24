package bruno.kmp.data.remote

import bruno.kmp.domain.mapper.ApiCharacterMapper
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataImpTest {

    private lateinit var httpClient: HttpClient
    private lateinit var remoteDataImp: RemoteDataImp
    private lateinit var mockEngine: MockEngine

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Set up MockEngine with predefined responses
        mockEngine = MockEngine { request ->
            when (request.url.toString()) {
                "https://example.com/api/character?page=1" -> {
                    respond(
                        content = """
                            {
                                "results": [
                                    {
                                        "id": 1,
                                        "name": "Rick Sanchez",
                                        "status": "Alive",
                                        "species": "Human",
                                        "gender": "Male",
                                        "type": "",
                                        "origin": {"name": "Earth"},
                                        "location": {"name": "Earth"},
                                        "image": "rick.jpg"
                                    },
                                    {
                                        "id": 2,
                                        "name": "Morty Smith",
                                        "status": "Alive",
                                        "species": "Human",
                                        "gender": "Male",
                                        "type": "",
                                        "origin": {"name": "Earth"},
                                        "location": {"name": "Earth"},
                                        "image": "morty.jpg"
                                    }
                                ]
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                "https://example.com/api/character?page=1&name=Rick" -> {
                    respond(
                        content = """
                            {
                                "results": [
                                    {
                                        "id": 1,
                                        "name": "Rick Sanchez",
                                        "status": "Alive",
                                        "species": "Human",
                                        "gender": "Male",
                                        "type": "",
                                        "origin": {"name": "Earth"},
                                        "location": {"name": "Earth"},
                                        "image": "rick.jpg"
                                    }
                                ]
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                "https://example.com/api/character?page=1&status=Alive" -> {
                    respond(
                        content = """
                            {
                                "results": [
                                    {
                                        "id": 1,
                                        "name": "Rick Sanchez",
                                        "status": "Alive",
                                        "species": "Human",
                                        "gender": "Male",
                                        "type": "",
                                        "origin": {"name": "Earth"},
                                        "location": {"name": "Earth"},
                                        "image": "rick.jpg"
                                    },
                                    {
                                        "id": 2,
                                        "name": "Morty Smith",
                                        "status": "Alive",
                                        "species": "Human",
                                        "gender": "Male",
                                        "type": "",
                                        "origin": {"name": "Earth"},
                                        "location": {"name": "Earth"},
                                        "image": "morty.jpg"
                                    }
                                ]
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                "https://example.com/api/character/1" -> {
                    respond(
                        content = """
                            {
                                "id": 1,
                                "name": "Rick Sanchez",
                                "status": "Alive",
                                "species": "Human",
                                "gender": "Male",
                                "type": "",
                                "origin": {"name": "Earth"},
                                "location": {"name": "Earth"},
                                "image": "rick.jpg"
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                "https://example.com/api/character/999" -> {
                    respond(
                        content = """{"error": "Character not found"}""",
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> {
                    respond(
                        content = """{"error": "Bad Request"}""",
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }

        // Configure HttpClient with MockEngine and JSON serialization
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json()
            }
        }

        remoteDataImp = RemoteDataImp(
            endPoint = "https://example.com",
            httpClient = httpClient,
            apiCharacterMapper = ApiCharacterMapper()
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        httpClient.close()
    }

    @Test
    fun `getCharactersFromApi returns list of characters for given page`() = runTest {
        // Arrange
        val page = "1"
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "Human",
                gender = Gender.MALE,
                type = "",
                origin = "Earth",
                location = "Earth",
                image = "rick.jpg",
                page = ""
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = Status.ALIVE,
                species = "Human",
                gender = Gender.MALE,
                type = "",
                origin = "Earth",
                location = "Earth",
                image = "morty.jpg",
                page = ""
            )
        )

        // Act
        val characters = remoteDataImp.getCharactersFromApi(page)

        // Assert
        assertEquals(expectedCharacters, characters)
    }

    @Test
    fun `getSearchedCharactersFromApi returns list of characters matching name`() = runTest {
        // Arrange
        val page = "1"
        val name = "Rick"
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "Human",
                gender = Gender.MALE,
                type = "",
                origin = "Earth",
                location = "Earth",
                image = "rick.jpg",
                page = ""
            )
        )

        // Act
        val characters = remoteDataImp.getSearchedCharactersFromApi(page, name)

        // Assert
        assertEquals(expectedCharacters, characters)
    }

    @Test
    fun `getCharactersByStatusFromApi returns list of characters matching status`() = runTest {
        // Arrange
        val page = "1"
        val status = "Alive"
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = "Human",
                gender = Gender.MALE,
                type = "",
                origin = "Earth",
                location = "Earth",
                image = "rick.jpg",
                page = ""
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = Status.ALIVE,
                species = "Human",
                gender = Gender.MALE,
                type = "",
                origin = "Earth",
                location = "Earth",
                image = "morty.jpg",
                page = ""
            )
        )

        // Act
        val characters = remoteDataImp.getCharactersByStatusFromApi(page, status)

        // Assert
        assertEquals(expectedCharacters, characters)
    }

    @Test
    fun `getCharacterFromApi returns character for given ID`() = runTest {
        // Arrange
        val characterId = 1
        val expectedCharacter = Character(
            id = 1,
            name = "Rick Sanchez",
            status = Status.ALIVE,
            species = "Human",
            gender = Gender.MALE,
            type = "",
            origin = "Earth",
            location = "Earth",
            image = "rick.jpg",
            page = ""
        )

        // Act
        val character = remoteDataImp.getCharacterFromApi(characterId)

        // Assert
        assertEquals(expectedCharacter, character)
    }

    @Test
    fun `getCharacterFromApi throws exception for invalid ID`() = runTest {
        // Arrange
        val characterId = 999

        // Act & Assert
        assertFails {
            remoteDataImp.getCharacterFromApi(characterId)
        }
    }
}