package io.sws.myanimetracker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import myanimetracker.shared.generated.resources.Res
import myanimetracker.shared.generated.resources.ic_bookmark
import myanimetracker.shared.generated.resources.ic_home
import myanimetracker.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import io.sws.myanimetracker.presentation.animation.LocalAnimatedVisibilityScope
import io.sws.myanimetracker.presentation.animation.LocalSharedTransitionScope
import io.sws.myanimetracker.presentation.components.BottomNavBar
import io.sws.myanimetracker.presentation.components.NavItem
import io.sws.myanimetracker.presentation.navigation.DetailRoute
import io.sws.myanimetracker.presentation.navigation.HomeRoute
import io.sws.myanimetracker.presentation.navigation.Navigator
import io.sws.myanimetracker.presentation.navigation.SearchRoute
import io.sws.myanimetracker.presentation.navigation.TrackedRoute
import io.sws.myanimetracker.presentation.navigation.BrowseRoute
import io.sws.myanimetracker.presentation.screen.browse.BrowseScreen
import io.sws.myanimetracker.presentation.screen.detail.DetailScreen
import io.sws.myanimetracker.presentation.screen.home.HomeScreen
import io.sws.myanimetracker.presentation.screen.search.SearchScreen
import io.sws.myanimetracker.presentation.screen.tracked.TrackedScreen
import io.sws.myanimetracker.presentation.theme.LocalBrutalColors
import io.sws.myanimetracker.presentation.theme.BrutalTheme
import org.koin.compose.koinInject

@Composable
fun App() {
    BrutalTheme {
        val navigator = koinInject<Navigator>()
        val backStack by navigator.backStack.collectAsState()
        val colors = LocalBrutalColors.current
        val currentRoute = backStack.lastOrNull()
        val showBottomBar = currentRoute !is DetailRoute && currentRoute !is BrowseRoute

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colors.background)
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        val slideIn = slideInVertically(
                            animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f),
                            initialOffsetY = { it }
                        ) + fadeIn(animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f))
                        val slideOut = slideOutVertically(
                            animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f),
                            targetOffsetY = { it }
                        ) + fadeOut(animationSpec = spring(stiffness = 380f, dampingRatio = 0.9f))
                        slideIn togetherWith slideOut
                    },
                    label = "screenTransition",
                    modifier = Modifier.fillMaxSize()
                ) { route ->
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this@SharedTransitionLayout,
                        LocalAnimatedVisibilityScope provides this@AnimatedContent
                    ) {
                        when (route) {
                            is HomeRoute -> HomeScreen(
                                onSearchClick = { navigator.navigateRoot(SearchRoute) },
                                onBrowse = { navigator.navigate(BrowseRoute(it)) }
                            )
                            is SearchRoute -> SearchScreen()
                            is TrackedRoute -> TrackedScreen()
                            is BrowseRoute -> BrowseScreen(
                                category = route.category,
                                onBack = { navigator.navigateBack() }
                            )
                            is DetailRoute -> DetailScreen(malId = route.malId, initialAnime = route.anime)
                            null -> Unit
                        }
                    }
                }
            }

            if (showBottomBar) {
                val navItems = listOf(
                    NavItem(label = "Home", icon = painterResource(Res.drawable.ic_home)),
                    NavItem(label = "Search", icon = painterResource(Res.drawable.ic_search)),
                    NavItem(label = "My List", icon = painterResource(Res.drawable.ic_bookmark))
                )
                val routes = listOf(HomeRoute, SearchRoute, TrackedRoute)
                BottomNavBar(
                    items = navItems,
                    selectedIndex = routes.indexOfFirst { it == currentRoute }.coerceAtLeast(0),
                    onSelect = { navigator.navigateRoot(routes[it]) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
