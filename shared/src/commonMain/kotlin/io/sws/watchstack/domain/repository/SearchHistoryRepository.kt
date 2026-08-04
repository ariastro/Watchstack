package io.sws.watchstack.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun observeRecent(): Flow<List<String>>
    suspend fun addQuery(query: String)
    suspend fun clear()
}
