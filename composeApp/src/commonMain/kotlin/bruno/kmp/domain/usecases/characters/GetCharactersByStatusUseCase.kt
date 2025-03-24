package bruno.kmp.domain.usecases.characters

import bruno.kmp.domain.contracts.IRepository
import bruno.kmp.domain.usecases.base.BaseUseCase
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.StatusParams
import kotlinx.coroutines.CoroutineDispatcher

class GetCharactersByStatusUseCase(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : BaseUseCase<StatusParams, List<Character>>(dispatcher){
    override suspend fun block(param: StatusParams): List<Character>
        = repository.getCharactersByStatus(param.page, param.status)
}