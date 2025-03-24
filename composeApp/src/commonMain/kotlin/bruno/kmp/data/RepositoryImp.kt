package bruno.kmp.data

import bruno.kmp.domain.contracts.ICacheData
import bruno.kmp.domain.contracts.IRepository
import bruno.kmp.domain.contracts.IRemoteData
import bruno.kmp.domain.model.Character
import kotlinx.coroutines.flow.first

class RepositoryImp(
    private val cacheData: ICacheData,
    private val remoteData: IRemoteData,
) : IRepository {

    override suspend fun getCharacters(page: String): List<Character> {
        var apiData = listOf<Character>()

        apiData = cacheData.getCharactersByPage(page).first()
        if (apiData.isEmpty()) {
            apiData = remoteData.getCharactersFromApi(page)
            insertCharacters(apiData, page)
        }
        return apiData
    }

    override suspend fun insertCharacters(characters: List<Character>, page: String) {
        cacheData.removeCharactersByPage(page)
        characters.map { character ->
            character.page = page
            cacheData.addCharacterToCache(character)
        }
    }

    override suspend fun getSearchedCharacters(page: String, name: String): List<Character> {
        return remoteData.getSearchedCharactersFromApi(page, name)
    }

    override suspend fun getCharactersByStatus(page: String, status: String): List<Character> {
        return remoteData.getCharactersByStatusFromApi(page, status)
    }

    override suspend fun getCharacterDetail(id: Int): Character {
        return remoteData.getCharacterFromApi(id)
    }
}