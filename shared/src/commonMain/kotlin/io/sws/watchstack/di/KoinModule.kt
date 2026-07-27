package io.sws.watchstack.di

import io.sws.watchstack.data.local.MemoryAnimeCache
import io.sws.watchstack.data.local.RemoteAnimeCacheDataSource
import io.sws.watchstack.data.local.SearchHistoryStore
import io.sws.watchstack.data.local.TrackedAnimeLocalDataSource
import io.sws.watchstack.data.remote.JikanApi
import io.sws.watchstack.data.remote.createHttpClient
import io.sws.watchstack.data.repository.AnimeRepositoryImpl
import io.sws.watchstack.data.repository.TrackedAnimeRepositoryImpl
import io.sws.watchstack.domain.repository.AnimeRepository
import io.sws.watchstack.domain.repository.SearchHistoryRepository
import io.sws.watchstack.domain.repository.TrackedAnimeRepository
import io.sws.watchstack.domain.usecase.AddSearchHistoryUseCase
import io.sws.watchstack.domain.usecase.ClearSearchHistoryUseCase
import io.sws.watchstack.domain.usecase.GetAiringNowUseCase
import io.sws.watchstack.domain.usecase.GetAnimeDetailsUseCase
import io.sws.watchstack.domain.usecase.GetCharactersUseCase
import io.sws.watchstack.domain.usecase.GetLibraryStatsUseCase
import io.sws.watchstack.domain.usecase.GetRecommendationsUseCase
import io.sws.watchstack.domain.usecase.GetSeasonalAnimeUseCase
import io.sws.watchstack.domain.usecase.GetTopAnimeUseCase
import io.sws.watchstack.domain.usecase.GetTrackedAnimeUseCase
import io.sws.watchstack.domain.usecase.GetTrackedByMalIdUseCase
import io.sws.watchstack.domain.usecase.GetUpcomingUseCase
import io.sws.watchstack.domain.usecase.ObserveSearchHistoryUseCase
import io.sws.watchstack.domain.usecase.RemoveTrackedAnimeUseCase
import io.sws.watchstack.domain.usecase.SearchAnimeUseCase
import io.sws.watchstack.domain.usecase.TrackAnimeUseCase
import io.sws.watchstack.domain.usecase.UpdateTrackedAnimeUseCase
import io.sws.watchstack.presentation.navigation.Navigator
import io.sws.watchstack.presentation.screen.browse.BrowseCategory
import io.sws.watchstack.presentation.screen.browse.BrowseViewModel
import io.sws.watchstack.presentation.screen.detail.AnimeDetailViewModel
import io.sws.watchstack.presentation.screen.home.HomeViewModel
import io.sws.watchstack.presentation.screen.search.SearchViewModel
import io.sws.watchstack.presentation.screen.settings.SettingsViewModel
import io.sws.watchstack.presentation.screen.stats.StatsViewModel
import io.sws.watchstack.presentation.screen.tracked.TrackedViewModel
import io.sws.watchstack.presentation.theme.ThemeController
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
