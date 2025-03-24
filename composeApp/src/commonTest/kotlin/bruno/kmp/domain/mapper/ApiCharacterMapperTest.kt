package bruno.kmp.domain.mapper

import bruno.kmp.data.remote.ApiCharacter
import bruno.kmp.data.remote.Location
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiCharacterMapperTest {

    private lateinit var mapper: ApiCharacterMapper

    @BeforeTest
    fun setUp() {
        mapper = ApiCharacterMapper()
    }

    @Test
    fun `map converts ApiCharacter to Character with status ALIVE`() {
        // Arrange
        val apiCharacter = ApiCharacter(
            id = 1,
            name = "Rick",
            status = "Alive",
            species = "Human",
            gender = "Male",
            type = "Scientist",
            origin = Location(name = "Earth"),
            location = Location(name = "Space"),
            image = "rick.jpg"
        )
        val expectedCharacter = Character(
            id = 1,
            name = "Rick",
            status = Status.ALIVE,
            species = "Human",
            gender = Gender.MALE,
            type = "Scientist",
            origin = "Earth",
            location = "Space",
            image = "rick.jpg",
            page = ""
        )

        // Act
        val result = mapper.map(apiCharacter)

        // Assert
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `map converts ApiCharacter to Character with status DEAD`() {
        // Arrange
        val apiCharacter = ApiCharacter(
            id = 2,
            name = "Birdperson",
            status = "Dead",
            species = "Birdperson",
            gender = "Male",
            type = "",
            origin = Location(name = "Bird World"),
            location = Location(name = "Unknown"),
            image = "birdperson.jpg"
        )
        val expectedCharacter = Character(
            id = 2,
            name = "Birdperson",
            status = Status.DEAD,
            species = "Birdperson",
            gender = Gender.MALE,
            type = "",
            origin = "Bird World",
            location = "Unknown",
            image = "birdperson.jpg",
            page = ""
        )

        // Act
        val result = mapper.map(apiCharacter)

        // Assert
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `map converts ApiCharacter to Character with status UNKNOWN`() {
        // Arrange
        val apiCharacter = ApiCharacter(
            id = 3,
            name = "Mr. Meeseeks",
            status = "Unknown",
            species = "Meeseeks",
            gender = "Male",
            type = "Helper",
            origin = Location(name = "Meeseeks Box"),
            location = Location(name = "Earth"),
            image = "meeseeks.jpg"
        )
        val expectedCharacter = Character(
            id = 3,
            name = "Mr. Meeseeks",
            status = Status.UNKNOWN,
            species = "Meeseeks",
            gender = Gender.MALE,
            type = "Helper",
            origin = "Meeseeks Box",
            location = "Earth",
            image = "meeseeks.jpg",
            page = ""
        )

        // Act
        val result = mapper.map(apiCharacter)

        // Assert
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `map converts ApiCharacter to Character with gender FEMALE`() {
        // Arrange
        val apiCharacter = ApiCharacter(
            id = 4,
            name = "Summer",
            status = "Alive",
            species = "Human",
            gender = "Female",
            type = "",
            origin = Location(name = "Earth"),
            location = Location(name = "Earth"),
            image = "summer.jpg"
        )
        val expectedCharacter = Character(
            id = 4,
            name = "Summer",
            status = Status.ALIVE,
            species = "Human",
            gender = Gender.FEMALE,
            type = "",
            origin = "Earth",
            location = "Earth",
            image = "summer.jpg",
            page = ""
        )

        // Act
        val result = mapper.map(apiCharacter)

        // Assert
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `map converts ApiCharacter to Character with gender GENDERLESS`() {
        // Arrange
        val apiCharacter = ApiCharacter(
            id = 5,
            name = "Unity",
            status = "Alive",
            species = "Hive Mind",
            gender = "Genderless",
            type = "",
            origin = Location(name = "Unknown"),
            location = Location(name = "Unknown"),
            image = "unity.jpg"
        )
        val expectedCharacter = Character(
            id = 5,
            name = "Unity",
            status = Status.ALIVE,
            species = "Hive Mind",
            gender = Gender.GENDERLESS,
            type = "",
            origin = "Unknown",
            location = "Unknown",
            image = "unity.jpg",
            page = ""
        )

        // Act
        val result = mapper.map(apiCharacter)

        // Assert
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `map converts ApiCharacter to Character with gender UNKNOWN`() {
        // Arrange
        val apiCharacter = ApiCharacter(
            id = 6,
            name = "Unknown Entity",
            status = "Alive",
            species = "Unknown",
            gender = "Other",
            type = "Unknown",
            origin = Location(name = "Unknown"),
            location = Location(name = "Unknown"),
            image = "unknown.jpg"
        )
        val expectedCharacter = Character(
            id = 6,
            name = "Unknown Entity",
            status = Status.ALIVE,
            species = "Unknown",
            gender = Gender.UNKNOWN,
            type = "Unknown",
            origin = "Unknown",
            location = "Unknown",
            image = "unknown.jpg",
            page = ""
        )

        // Act
        val result = mapper.map(apiCharacter)

        // Assert
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `map handles empty or null-like fields correctly`() {
        // Arrange
        val apiCharacter = ApiCharacter(
            id = 7,
            name = "",
            status = "",
            species = "",
            gender = "",
            type = "",
            origin = Location(name = ""),
            location = Location(name = ""),
            image = ""
        )
        val expectedCharacter = Character(
            id = 7,
            name = "",
            status = Status.UNKNOWN,
            species = "",
            gender = Gender.UNKNOWN,
            type = "",
            origin = "",
            location = "",
            image = "",
            page = ""
        )

        // Act
        val result = mapper.map(apiCharacter)

        // Assert
        assertEquals(expectedCharacter, result)
    }
}