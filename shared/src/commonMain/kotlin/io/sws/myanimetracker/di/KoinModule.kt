package io.sws.myanimetracker.di

import io.sws.myanimetracker.data.local.MemoryAnimeCache
import io.sws.myanimetracker.data.local.RemoteAnimeCacheDataSource
import io.sws.myanimetracker.data.local.SearchHistoryStore
import io.sws.myanimetracker.data.local.TrackedAnimeLocalDataSource
import io.sws.myanimetracker.data.remote.JikanApi
import io.sws.myanimetracker.data.remote.createHttpClient
import io.sws.myanimetracker.data.repository.AnimeRepositoryImpl
import io.sws.myanimetracker.data.repository.TrackedAnimeRepositoryImpl
import io.sws.myanimetracker.domain.repository.AnimeRepository
import io.sws.myanimetracker.domain.repository.SearchHistoryRepository
import io.sws.myanimetracker.domain.repository.TrackedAnimeRepository
import io.sws.myanimetracker.domain.usecase.AddSearchHistoryUseCase
import io.sws.myanimetracker.domain.usecase.ClearSearchHistoryUseCase
import io.sws.myanimetracker.domain.usecase.GetAiringNowUseCase
import io.sws.myanimetracker.domain.usecase.GetAnimeDetailsUseCase
import io.sws.myanimetracker.domain.usecase.GetCharactersUseCase
import io.sws.myanimetracker.domain.usecase.GetLibraryStatsUseCase
import io.sws.myanimetracker.domain.usecase.GetRecommendationsUseCase
import io.sws.myanimetracker.domain.usecase.GetSeasonalAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetTopAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetTrackedAnimeUseCase
import io.sws.myanimetracker.domain.usecase.GetTrackedByMalIdUseCase
import io.sws.myanimetracker.domain.usecase.GetUpcomingUseCase
import io.sws.myanimetracker.domain.usecase.ObserveSearchHistoryUseCase
import io.sws.myanimetracker.domain.usecase.RemoveTrackedAnimeUseCase
import io.sws.myanimetracker.domain.usecase.SearchAnimeUseCase
import io.sws.myanimetracker.domain.usecase.TrackAnimeUseCase
import io.sws.myanimetracker.domain.usecase.UpdateTrackedAnimeUseCase
import io.sws.myanimetracker.presentation.navigation.Navigator
import io.sws.myanimetracker.presentation.screen.browse.BrowseCategory
import io.sws.myanimetracker.presentation.screen.browse.BrowseViewModel
import io.sws.myanimetracker.presentation.screen.detail.AnimeDetailViewModel
import io.sws.myanimetracker.presentation.screen.home.HomeViewModel
import io.sws.myanimetracker.presentation.screen.search.SearchViewModel
import io.sws.myanimetracker.presentation.screen.settings.SettingsViewModel
import io.sws.myanimetracker.presentation.screen.stats.StatsViewModel
import io.sws.myanimetracker.presentation.screen.tracked.TrackedViewModel
import io.sws.myanimetracker.presentation.theme.ThemeController
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val navigationModule = module {
    single { Navigator() }
    single { ThemeController(get()) }
}

private val networkModule = module {
    single { createHttpClient() }
    single { JikanApi(get()) }
    single { MemoryAnimeCache() }
}

private val repositoryModule = module {
    single<AnimeRepository> { AnimeRepositoryImpl(get(), get(), get()) }
    single<TrackedAnimeRepository> { TrackedAnimeRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryStore(get()) }
}

private val dataSourceModule = module {
    single { TrackedAnimeLocalDataSource(get()) }
    single { RemoteAnimeCacheDataSource(get()) }
}

private val useCaseModule = module {
    factory { SearchAnimeUseCase(get()) }
    factory { GetAnimeDetailsUseCase(get()) }
    factory { GetTopAnimeUseCase(get()) }
    factory { GetAiringNowUseCase(get()) }
    factory { GetSeasonalAnimeUseCase(get()) }
    factory { GetUpcomingUseCase(get()) }
    factory { GetRecommendationsUseCase(get()) }
    factory { GetCharactersUseCase(get()) }
    factory { GetTrackedAnimeUseCase(get()) }
    factory { GetTrackedByMalIdUseCase(get()) }
    factory { TrackAnimeUseCase(get()) }
    factory { UpdateTrackedAnimeUseCase(get()) }
    factory { RemoveTrackedAnimeUseCase(get()) }
    factory { ObserveSearchHistoryUseCase(get()) }
    factory { AddSearchHistoryUseCase(get()) }
    factory { ClearSearchHistoryUseCase(get()) }
    factory { GetLibraryStatsUseCase(get()) }
}

private val viewModelModule = module {
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { AnimeDetailViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { TrackedViewModel(get(), get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { (category: BrowseCategory) ->
        BrowseViewModel(category, get(), get(), get(), get(), get())
    }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { StatsViewModel(get(), get()) }
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
