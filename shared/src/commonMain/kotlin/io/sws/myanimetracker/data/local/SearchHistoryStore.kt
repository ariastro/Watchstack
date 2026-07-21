package io.sws.myanimetracker.data.local

import io.sws.myanimetracker.core.currentTimeMillis
import io.sws.myanimetracker.db.AnimeDatabase
import io.sws.myanimetracker.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SearchHistoryStore(
    private val database: AnimeDatabase,
    private val maxItems: Int = 12
) : SearchHistoryRepository {
    private val queries get() = database.animeDatabaseQueries
    private val _recent = MutableStateFlow(loadRecent())

    override fun observeRecent(): Flow<List<String>> = _recent.asStateFlow()

    override suspend fun addQuery(query: String) {
        val cleaned = query.trim()
        if (cleaned.length < 2) return
        withContext(Dispatchers.IO) {
            queries.upsertSearchHistory(cleaned, currentTimeMillis())
            val all = queries.getAllSearchHistoryKeys().executeAsList()
            if (all.size > maxItems) {
                all.drop(maxItems).forEach { queries.deleteSearchHistoryQuery(it) }
            }
            _recent.value = loadRecent()
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            queries.clearSearchHistory()
            _recent.value = emptyList()
        }
    }

    private fun loadRecent(): List<String> =
        runCatching { queries.getSearchHistory(maxItems.toLong()).executeAsList() }
            .getOrDefault(emptyList())
}
