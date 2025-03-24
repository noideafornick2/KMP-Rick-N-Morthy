package bruno.kmp.data.mocks

import bruno.kmp.domain.contracts.ICacheData
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockCacheData : ICacheData {

    override suspend fun getCharactersByPage(page: String): Flow<List<Character>> {
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
        return flowOf(expectedCharacters)
    }

    override suspend fun removeCharactersByPage(page: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addCharacterToCache(character: Character) = throw NotImplementedError()
}
