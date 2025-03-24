package bruno.kmp.domain.contracts

import bruno.kmp.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getCharacters(page: String): List<Character>
    suspend fun getSearchedCharacters(page: String, name: String): List<Character>
    suspend fun getCharactersByStatus(page: String, status: String): List<Character>
    suspend fun insertCharacters(characters: List<Character>, page: String)

    suspend fun getCharacterDetail(id: Int): Character
}