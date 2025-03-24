package bruno.kmp.data.local

import app.cash.sqldelight.coroutines.asFlow
import bruno.kmp.data.local.sqldelight.SharedDatabase
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status
import bruno.kmp.domain.contracts.ICacheData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CacheDataImp(
    private val sharedDatabase: SharedDatabase,
) : ICacheData {

    override suspend fun addCharacterToCache(character: Character) {
        sharedDatabase {
            it.appDatabaseQueries.insertCharacter(
                character.id.toLong(),
                character.name,
                character.status,
                character.species,
                character.gender,
                character.type,
                character.origin,
                character.location,
                character.image,
                character.page
            )
        }
    }

    override suspend fun removeCharactersByPage(page: String) {
        sharedDatabase {
            it.appDatabaseQueries.removeCharacterByPage(page)
        }
    }

    override suspend fun getCharactersByPage(page: String): Flow<List<Character>> {
        return sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.selectAllCharactersByPage(page,::mapCharacter).asFlow()
                .map { it.executeAsList() }
        }
    }

    private fun mapCharacter(
        id: Long,
        name: String,
        status: Status,
        species: String,
        gender: Gender,
        type: String,
        origin: String,
        location: String,
        image: String,
        page: String
    ): Character = Character(
        id.toInt(),
        name,
        status,
        species,
        gender,
        type,
        origin,
        location,
        image,
        page
    )
}