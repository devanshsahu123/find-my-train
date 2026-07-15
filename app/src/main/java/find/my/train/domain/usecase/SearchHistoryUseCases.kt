package find.my.train.domain.usecase

import find.my.train.domain.model.SearchHistory
import find.my.train.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<SearchHistory>> = repository.getRecentSearches(limit)
}

class SaveSearchHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(query: String, type: String) {
        if (query.trim().isNotEmpty()) {
            repository.saveSearch(query.trim(), type)
        }
    }
}

class ClearSearchHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke() = repository.clearHistory()
}
