package io.sws.myanimetracker

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.sws.myanimetracker.presentation.animation.LocalSharedTransitionScope
import io.sws.myanimetracker.presentation.components.BottomNavBar
import io.sws.myanimetracker.presentation.components.NavItem
import io.sws.myanimetracker.presentation.navigation.BrowseRoute
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.HomeRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import io.sws.myanimetracker.presentation.navigation.SearchRoute
import io.sws.myanimetracker.presentation.navigation.SettingsRoute
import io.sws.myanimetracker.presentation.navigation.StatsRoute
import io.sws.myanimetracker.presentation.navigation.TopLevelRoutes
import io.sws.myanimetracker.presentation.navigation.TrackedRoute
import io.sws.myanimetracker.presentation.screen.browse.BrowseScreen
import io.sws.myanimetracker.presentation.screen.detail.DetailScreen
import io.sws.myanimetracker.presentation.screen.home.HomeScreen
import io.sws.myanimetracker.presentation.screen.search.SearchScreen
import io.sws.myanimetracker.presentation.screen.settings.SettingsScreen
import io.sws.myanimetracker.presentation.screen.stats.StatsScreen
import io.sws.myanimetracker.presentation.screen.tracked.TrackedScreen
import io.sws.myanimetracker.presentation.theme.ApplySystemBars
import io.sws.myanimetracker.presentation.theme.BrutalTheme
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.ThemeController
import io.sws.myanimetracker.presentation.theme.resolveDark
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_bookmark
import myanimetracker.shared.generated.resources.ic_home
import myanimetracker.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun App() {
    val themeController = koinInject<ThemeController>()
    val themeMode by themeController.themeMode.collectAsState()
    val darkTheme = themeMode.resolveDark(systemDark = isSystemInDarkTheme())

    BrutalTheme(darkTheme = darkTheme) {
        val navigator = koinInject<Navigator>()
        val colors = LocalBrutalColors.current
        val currentRoute = navigator.currentRoute
        val showBottomBar = currentRoute in TopLevelRoutes
        val navBarPadding = WindowInsets.navigationBars.asPaddingValues()

        ApplySystemBars(darkTheme = darkTheme, background = colors.background)

        // Status bar transparent — screens draw under it.
        // System nav pad only for tab roots (bottom dock). Nested screens handle own bottom insets
        // so action bars can paint continuous under the gesture/nav area.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colors.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (showBottomBar) {
                            Modifier.padding(bottom = navBarPadding.calculateBottomPadding())
                        } else {
                            Modifier
                        }
                    )
            ) {
                SharedTransitionLayout {
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this@SharedTransitionLayout
                    ) {
                        NavDisplay(
                            backStack = navigator.backStack,
                            onBack = { navigator.navigateBack() },
                            transitionSpec = {
                                slideInHorizontally(
                                    animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f),
                                    initialOffsetX = { it / 5 }
                                ) + fadeIn() togetherWith
                                    slideOutHorizontally(
                                        animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f),
                                        targetOffsetX = { -it / 8 }
                                    ) + fadeOut()
                            },
                            popTransitionSpec = {
                                slideInHorizontally(
                                    animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f),
                                    initialOffsetX = { -it / 8 }
                                ) + fadeIn() togetherWith
                                    slideOutHorizontally(
                                        animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f),
                                        targetOffsetX = { it / 5 }
                                    ) + fadeOut()
                            },
                            entryProvider = entryProvider {
                                entry<HomeRoute> {
                                    HomeScreen()
                                }
                                entry<SearchRoute> {
                                    SearchScreen()
                                }
                                entry<TrackedRoute> {
                                    TrackedScreen()
                                }
                                entry<SettingsRoute> {
                                    SettingsScreen()
                                }
                                entry<StatsRoute> {
                                    StatsScreen()
                                }
                                entry<BrowseRoute> { key ->
                                    BrowseScreen(category = key.category)
                                }
                                entry<DetailRoute> { key ->
                                    DetailScreen(
                                        malId = key.malId,
                                        initialAnime = key.anime
                                    )
                                }
                            }
                        )
                    }
                }

                if (showBottomBar) {
                    val navItems = listOf(
                        NavItem(label = "Home", icon = painterResource(Res.drawable.ic_home)),
                        NavItem(label = "Search", icon = painterResource(Res.drawable.ic_search)),
                        NavItem(label = "List", icon = painterResource(Res.drawable.ic_bookmark))
                    )
                    val routes = TopLevelRoutes
                    BottomNavBar(
                        items = navItems,
                        selectedIndex = routes.indexOf(navigator.topLevelKey).coerceAtLeast(0),
                        onSelect = { navigator.navigateRoot(routes[it]) },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}
