package find.my.train.data.repository

import find.my.train.data.local.dao.SearchHistoryDao
import find.my.train.data.local.entity.RecentSearchEntity
import find.my.train.domain.model.SearchHistory
import find.my.train.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : HistoryRepository {
    override fun getRecentSearches(limit: Int): Flow<List<SearchHistory>> {
        return searchHistoryDao.getRecentSearches(limit).map { entities ->
            entities.map { SearchHistory(it.id, it.query, it.type, it.timestamp) }
        }
    }

    override suspend fun saveSearch(query: String, type: String) {
        searchHistoryDao.insertSearch(
            RecentSearchEntity(
                query = query,
                type = type,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun clearHistory() {
        searchHistoryDao.clearSearchHistory()
    }
}
