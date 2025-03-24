package bruno.kmp.domain.usecases.characters

import bruno.kmp.domain.contracts.IRepository
import bruno.kmp.domain.usecases.base.BaseUseCase
import bruno.kmp.domain.model.Character
import kotlinx.coroutines.CoroutineDispatcher

class GetCharactersUseCase(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : BaseUseCase<String, List<Character>>(dispatcher){
    override suspend fun block(param: String): List<Character>
        = repository.getCharacters(param)
}