package bruno.kmp.domain.contracts

import bruno.kmp.domain.model.Character

interface IRemoteData {
    suspend fun getCharactersFromApi(page: String = "1"): List<Character>
    suspend fun getSearchedCharactersFromApi(page: String = "1", name: String): List<Character>
    suspend fun getCharactersByStatusFromApi(page: String = "1", status: String): List<Character>
    suspend fun getCharacterFromApi(id: Int): Character
}