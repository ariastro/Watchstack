package io.sws.watchstack.presentation.animation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Composition locals exposing the shared-transition scopes so any deep
 * composable (poster cards, detail banner) can opt into a shared-element
 * flight without threading the scope through every call site.
 */
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
