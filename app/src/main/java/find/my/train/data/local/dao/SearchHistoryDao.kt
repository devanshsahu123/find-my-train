package find.my.train.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import find.my.train.data.local.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: RecentSearchEntity)

    @Query("SELECT * FROM recent_searches ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSearches(limit: Int): Flow<List<RecentSearchEntity>>

    @Query("DELETE FROM recent_searches")
    suspend fun clearSearchHistory()
}
