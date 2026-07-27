package io.sws.watchstack.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.presentation.screen.browse.BrowseCategory
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute : NavKey
@Serializable data object SearchRoute : NavKey
@Serializable data object TrackedRoute : NavKey
@Serializable data object SettingsRoute : NavKey
@Serializable data object StatsRoute : NavKey
@Serializable data class BrowseRoute(val category: BrowseCategory) : NavKey
@Serializable data class DetailRoute(val malId: Int, val anime: Anime? = null) : NavKey

val TopLevelRoutes: List<NavKey> = listOf(HomeRoute, SearchRoute, TrackedRoute)

/**
 * Navigation 3-style top-level + nested back stacks.
 * Tabs keep independent stacks; nested screens (Settings/Detail/…) push onto current tab.
 * Back never empties the start tab — app only exits when system back fires on lone Home.
 */
class Navigator(private val startKey: NavKey = HomeRoute) {
    private val topLevelStacks: LinkedHashMap<NavKey, SnapshotStateList<NavKey>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    var topLevelKey by mutableStateOf(startKey)
        private set

    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(startKey)

    val currentRoute: NavKey
        get() = backStack.lastOrNull() ?: startKey

    private fun rebuildBackStack() {
        backStack.clear()
        topLevelStacks.values.forEach { stack -> backStack.addAll(stack) }
    }

    fun navigate(route: NavKey) {
        if (route in TopLevelRoutes) {
            navigateRoot(route)
            return
        }
        val stack = topLevelStacks[topLevelKey] ?: return
        if (stack.lastOrNull() == route) return
        stack.add(route)
        rebuildBackStack()
    }

    fun navigateRoot(route: NavKey) {
        require(route in TopLevelRoutes) { "navigateRoot only for top-level tabs, got $route" }
        if (topLevelStacks[route] == null) {
            topLevelStacks[route] = mutableStateListOf(route)
        } else {
            topLevelStacks.remove(route)?.let { existing ->
                topLevelStacks[route] = existing
            }
        }
        topLevelKey = route
        rebuildBackStack()
    }

    fun navigateBack(): Boolean {
        val stack = topLevelStacks[topLevelKey] ?: return false
        if (stack.size > 1) {
            stack.removeLastOrNull()
            rebuildBackStack()
            return true
        }
        // At tab root: switch to Home if on another tab; Home root → not handled (system exit)
        if (topLevelKey != HomeRoute) {
            topLevelStacks.remove(topLevelKey)
            topLevelKey = topLevelStacks.keys.lastOrNull() ?: HomeRoute
            if (topLevelStacks[HomeRoute] == null) {
                topLevelStacks[HomeRoute] = mutableStateListOf(HomeRoute)
                topLevelKey = HomeRoute
            }
            rebuildBackStack()
            return true
        }
        return false
    }

    fun canNavigateBack(): Boolean {
        val stack = topLevelStacks[topLevelKey] ?: return false
        return stack.size > 1 || topLevelKey != HomeRoute
    }
}
