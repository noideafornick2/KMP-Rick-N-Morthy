package bruno.kmp.domain.contracts

import bruno.kmp.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface ICacheData {
    suspend fun addCharacterToCache(character: Character)
    suspend fun removeCharactersByPage(page: String)
    suspend fun getCharactersByPage(page: String): Flow<List<Character>>
}