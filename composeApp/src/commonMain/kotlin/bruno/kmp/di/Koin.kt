package bruno.kmp.di

import bruno.kmp.data.local.CacheDataImp
import bruno.kmp.data.local.sqldelight.SharedDatabase
import bruno.kmp.data.remote.RemoteDataImp
import bruno.kmp.domain.mapper.ApiCharacterMapper
import bruno.kmp.domain.contracts.IRepository
import bruno.kmp.domain.usecases.detail.GetCharacterDetailUseCase
import bruno.kmp.domain.usecases.characters.GetCharactersByStatusUseCase
import bruno.kmp.domain.usecases.characters.GetCharactersUseCase
import bruno.kmp.domain.usecases.characters.GetSearchedCharactersUseCase
import bruno.kmp.domain.contracts.ICacheData
import bruno.kmp.domain.contracts.IRemoteData
import bruno.kmp.data.RepositoryImp
import bruno.kmp.presentation.character_detail.CharacterDetailViewModel
import bruno.kmp.presentation.characters.CharactersViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            viewModelModule,
            useCasesModule,
            repositoryModule,
            ktorModule,
            sqlDelightModule,
            mapperModule,
            dispatcherModule,
            platformModule()
        )
    }

val viewModelModule = module {
    factory { CharactersViewModel(get(), get(), get()) }
    factory { params -> CharacterDetailViewModel(get(), get()) }
}

val useCasesModule: Module = module {
    factory { GetCharactersUseCase(get(), get()) }
    factory { GetCharactersByStatusUseCase(get(), get()) }
    factory { GetSearchedCharactersUseCase(get(), get()) }
    factory { GetCharacterDetailUseCase(get(), get()) }
}

val repositoryModule = module {
    single<IRepository> { RepositoryImp(get(), get()) }
    single<ICacheData> { CacheDataImp(get()) }
    single<IRemoteData> { RemoteDataImp(get(), get(), get()) }


}

val ktorModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    single { "https://rickandmortyapi.com" }
}

val sqlDelightModule = module {
    single { SharedDatabase(get()) }
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}

val mapperModule = module {
    factory { ApiCharacterMapper() }
}

fun initKoin() = initKoin {}
