package io.sws.myanimetracker.presentation.navigation

import io.sws.myanimetracker.domain.model.Anime
import io.sws.myanimetracker.presentation.screen.browse.BrowseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute : NavRoute
@Serializable data object SearchRoute : NavRoute
@Serializable data object TrackedRoute : NavRoute
@Serializable data class BrowseRoute(val category: BrowseCategory) : NavRoute
@Serializable data class DetailRoute(val malId: Int, val anime: Anime? = null) : NavRoute

sealed interface NavRoute

class Navigator {
    private val _backStack = MutableStateFlow<List<NavRoute>>(listOf(HomeRoute))
    val backStack: StateFlow<List<NavRoute>> = _backStack.asStateFlow()

    val currentRoute: NavRoute get() = _backStack.value.last()

    fun navigate(route: NavRoute) {
        _backStack.value = _backStack.value + route
    }

    fun navigateBack() {
        val list = _backStack.value
        if (list.size > 1) {
            _backStack.value = list.dropLast(1)
        }
    }

    fun navigateRoot(route: NavRoute) {
        _backStack.value = listOf(route)
    }
}
