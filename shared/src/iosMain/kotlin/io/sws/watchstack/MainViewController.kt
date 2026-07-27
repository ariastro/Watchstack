package io.sws.watchstack

import androidx.compose.ui.window.ComposeUIViewController
import io.sws.watchstack.data.local.DatabaseFactory
import io.sws.watchstack.data.local.ThemePreferences
import io.sws.watchstack.db.AnimeDatabase
import io.sws.watchstack.di.initKoin
import org.koin.dsl.module

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        koinStarted = true
        initKoin(
            module {
                single<AnimeDatabase> { DatabaseFactory.create() }
                single { ThemePreferences() }
            }
        )
    }
    App()
}
