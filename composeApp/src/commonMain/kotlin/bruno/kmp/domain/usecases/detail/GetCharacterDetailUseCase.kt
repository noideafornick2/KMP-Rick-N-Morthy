package bruno.kmp.domain.usecases.detail

import bruno.kmp.domain.contracts.IRepository
import bruno.kmp.domain.usecases.base.BaseUseCase
import bruno.kmp.domain.model.Character
import kotlinx.coroutines.CoroutineDispatcher

class GetCharacterDetailUseCase(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : BaseUseCase<Int, Character>(dispatcher){
    override suspend fun block(param: Int): Character = repository.getCharacterDetail(param)
}