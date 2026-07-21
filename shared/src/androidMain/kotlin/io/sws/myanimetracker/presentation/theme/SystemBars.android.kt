package io.sws.myanimetracker.presentation.theme

import android.app.Activity
import android.graphics.Color as AndroidColor
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun ApplySystemBars(
    darkTheme: Boolean,
    background: Color
) {
    val view = LocalView.current
    if (view.isInEditMode) return

    DisposableEffect(darkTheme, background) {
        val activity = view.context as? Activity ?: return@DisposableEffect onDispose { }
        val window = activity.window

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Fully transparent chrome so screen gradients paint under the status bar.
        @Suppress("DEPRECATION")
        window.statusBarColor = AndroidColor.TRANSPARENT
        @Suppress("DEPRECATION")
        window.navigationBarColor = AndroidColor.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }

        val lightIcons = !darkTheme && background.luminance() > 0.5f
        val transparent = AndroidColor.TRANSPARENT
        (activity as? ComponentActivity)?.enableEdgeToEdge(
            statusBarStyle = if (lightIcons) {
                SystemBarStyle.light(scrim = transparent, darkScrim = transparent)
            } else {
                SystemBarStyle.dark(scrim = transparent)
            },
            navigationBarStyle = if (lightIcons) {
                SystemBarStyle.light(scrim = transparent, darkScrim = transparent)
            } else {
                SystemBarStyle.dark(scrim = transparent)
            }
        )

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = lightIcons
            isAppearanceLightNavigationBars = lightIcons
        }

        onDispose { }
    }
}
