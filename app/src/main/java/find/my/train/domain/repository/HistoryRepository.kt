package find.my.train.domain.repository

import find.my.train.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getRecentSearches(limit: Int): Flow<List<SearchHistory>>
    suspend fun saveSearch(query: String, type: String)
    suspend fun clearHistory()
}
