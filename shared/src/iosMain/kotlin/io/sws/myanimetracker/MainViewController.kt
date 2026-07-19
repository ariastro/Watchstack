package io.sws.myanimetracker

import androidx.compose.ui.window.ComposeUIViewController
import io.sws.myanimetracker.data.local.DatabaseFactory
import io.sws.myanimetracker.db.AnimeDatabase
import io.sws.myanimetracker.di.initKoin
import org.koin.dsl.module

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        koinStarted = true
        initKoin(
            module {
                single<AnimeDatabase> { DatabaseFactory.create() }
            }
        )
    }
    App()
}
