package bruno.kmp.domain.usecases.characters

import bruno.kmp.domain.contracts.IRepository
import bruno.kmp.domain.usecases.base.BaseUseCase
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.SearchParams
import kotlinx.coroutines.CoroutineDispatcher

class GetSearchedCharactersUseCase(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : BaseUseCase<SearchParams, List<Character>>(dispatcher){
    override suspend fun block(param: SearchParams): List<Character>
        = repository.getSearchedCharacters(param.page, param.name)
}