package bruno.kmp.data.mocks

import bruno.kmp.domain.contracts.IRemoteData
import bruno.kmp.domain.model.Character

class MockRemoteData : IRemoteData {
    var characters: List<Character> = emptyList()
    var singleCharacter: Character? = null
    var shouldThrow: Exception? = null

    override suspend fun getCharactersFromApi(page: String): List<Character> {
        shouldThrow?.let { throw it }
        return characters
    }

    override suspend fun getSearchedCharactersFromApi(page: String, name: String): List<Character> {
        shouldThrow?.let { throw it }
        return characters
    }

    override suspend fun getCharactersByStatusFromApi(page: String, status: String): List<Character> {
        shouldThrow?.let { throw it }
        return characters
    }

    override suspend fun getCharacterFromApi(id: Int): Character {
        shouldThrow?.let { throw it }
        return singleCharacter ?: throw IllegalStateException("Character not found for id: $id")
    }
}