package bruno.kmp.data.remote

import bruno.kmp.domain.model.Character
import bruno.kmp.domain.contracts.IRemoteData
import bruno.kmp.domain.mapper.ApiCharacterMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RemoteDataImp(
    private val endPoint: String,
    private val httpClient: HttpClient,
    private val apiCharacterMapper: ApiCharacterMapper,
) : IRemoteData {
    override suspend fun getCharactersFromApi(page: String): List<Character> =
        apiCharacterMapper.map(
            (httpClient.get("$endPoint/api/character?page=$page").body<ApiCharactersResponse>()).results
        )

    override suspend fun getSearchedCharactersFromApi(page: String, name: String): List<Character> {
        return apiCharacterMapper.map(
            (httpClient.get("$endPoint/api/character?page=$page&name=$name").body<ApiCharactersResponse>()).results
        )
    }

    override suspend fun getCharactersByStatusFromApi(
        page: String,
        status: String
    ): List<Character> {
        return apiCharacterMapper.map(
            (httpClient.get("$endPoint/api/character?page=$page&status=$status").body<ApiCharactersResponse>()).results
        )
    }

    override suspend fun getCharacterFromApi(id: Int): Character =
        apiCharacterMapper.map(
            httpClient.get("$endPoint/api/character/$id").body<ApiCharacter>()
        )
}
