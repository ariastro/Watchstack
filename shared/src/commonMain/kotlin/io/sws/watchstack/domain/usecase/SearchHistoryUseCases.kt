package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

class ObserveSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    operator fun invoke(): Flow<List<String>> = repository.observeRecent()
}

class AddSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke(query: String) = repository.addQuery(query)
}

class ClearSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke() = repository.clear()
}
