package io.sws.myanimetracker.di

import android.content.Context
import io.sws.myanimetracker.data.local.DatabaseFactory
import io.sws.myanimetracker.db.AnimeDatabase
import org.koin.dsl.*

fun initKoinAndroid(context: Context) {
    initKoin(
        module {
            single<Context> { context }
            single<AnimeDatabase> { DatabaseFactory.create(get()) }
        }
    )
}
