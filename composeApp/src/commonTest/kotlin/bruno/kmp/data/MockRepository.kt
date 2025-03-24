package bruno.kmp.data

import bruno.kmp.domain.contracts.IRepository
import bruno.kmp.domain.model.Character
import kotlinx.coroutines.flow.Flow

class MockRepository : IRepository {
    var character: Character? = null
    var characters: List<Character> = emptyList()
    var shouldThrow: Exception? = null

    override suspend fun getCharacters(page: String): List<Character> {
        shouldThrow?.let { throw it }
        return characters
    }

    override suspend fun getSearchedCharacters(
        page: String,
        name: String
    ): List<Character> {
        shouldThrow?.let { throw it }
        return characters
    }

    override suspend fun getCharactersByStatus(page: String, status: String): List<Character> {
        shouldThrow?.let { throw it }
        return characters
    }

    override suspend fun insertCharacters(
        characters: List<Character>,
        page: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacterDetail(id: Int): Character {
        shouldThrow?.let { throw it }
        return character ?: throw IllegalStateException("Character not found for id: $id")
    }

    override suspend fun getCharactersFavorites(): Flow<List<Character>> {
        TODO("Not yet implemented")
    }
}