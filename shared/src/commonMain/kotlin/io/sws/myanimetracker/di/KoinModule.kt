package io.sws.myanimetracker.di

import io.sws.myanimetracker.data.local.TrackedAnimeLocalDataSource
import io.sws.myanimetracker.data.remote.JikanApi
import io.sws.myanimetracker.data.remote.createHttpClient
import io.sws.myanimetracker.data.repository.AnimeRepositoryImpl
import io.sws.myanimetracker.data.repository.TrackedAnimeRepositoryImpl
import io.sws.myanimetracker.domain.repository.AnimeRepository
import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository
import io.sws.myanimetracker.domain.usecase.*
import io.sws.myanimetracker.presentation.navigation.Navigator
import io.sws.myanimetracker.presentation.screen.browse.BrowseViewModel
import io.sws.myanimetracker.presentation.screen.detail.AnimeDetailViewModel
import io.sws.myanimetracker.presentation.screen.home.HomeViewModel
import io.sws.myanimetracker.presentation.screen.search.SearchViewModel
import io.sws.myanimetracker.presentation.screen.tracked.TrackedViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val navigationModule = module {
    singleOf(::Navigator)
}

private val networkModule = module {
    singleOf(::createHttpClient)
    singleOf(::JikanApi)
}

private val repositoryModule = module {
    singleOf(::AnimeRepositoryImpl) bind AnimeRepository::class
    singleOf(::TrackedAnimeRepositoryImpl) bind TrackedAnimeRepository::class
}

private val dataSourceModule = module {
    singleOf(::TrackedAnimeLocalDataSource)
}

private val useCaseModule = module {
    factoryOf(::SearchAnimeUseCase)
    factoryOf(::GetAnimeDetailsUseCase)
    factoryOf(::GetTopAnimeUseCase)
    factoryOf(::GetAiringNowUseCase)
    factoryOf(::GetSeasonalAnimeUseCase)
    factoryOf(::GetUpcomingUseCase)
    factoryOf(::GetRecommendationsUseCase)
    factoryOf(::GetCharactersUseCase)
    factoryOf(::GetTrackedAnimeUseCase)
    factoryOf(::GetTrackedByMalIdUseCase)
    factoryOf(::TrackAnimeUseCase)
    factoryOf(::UpdateTrackedAnimeUseCase)
    factoryOf(::RemoveTrackedAnimeUseCase)
}

private val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::AnimeDetailViewModel)
    viewModelOf(::TrackedViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::BrowseViewModel)
}

fun initKoin(platformModule: org.koin.core.module.Module? = null) {
    startKoin {
        val modules = mutableListOf(
            navigationModule, networkModule, repositoryModule,
            dataSourceModule, useCaseModule, viewModelModule
        )
        platformModule?.let { modules.add(0, it) }
        modules(modules)
    }
}
