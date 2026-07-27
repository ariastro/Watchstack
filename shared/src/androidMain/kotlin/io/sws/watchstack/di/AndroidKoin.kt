package io.sws.watchstack.di

import android.content.Context
import io.sws.watchstack.data.local.DatabaseFactory
import io.sws.watchstack.data.local.ThemePreferences
import io.sws.watchstack.db.AnimeDatabase
import org.koin.dsl.module

fun initKoinAndroid(context: Context) {
    initKoin(
        module {
            single<Context> { context }
            single<AnimeDatabase> { DatabaseFactory.create(get()) }
            single { ThemePreferences(get()) }
        }
    )
}
