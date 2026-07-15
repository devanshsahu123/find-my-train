package find.my.train.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import find.my.train.data.local.entity.CachedLiveStatusEntity
import find.my.train.data.local.entity.SavedTrainEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedTrain(train: SavedTrainEntity)

    @Query("DELETE FROM saved_trains WHERE number = :number")
    suspend fun deleteSavedTrain(number: String)

    @Query("SELECT * FROM saved_trains ORDER BY savedAt DESC")
    fun getSavedTrains(): Flow<List<SavedTrainEntity>>

    @Query("SELECT COUNT(*) > 0 FROM saved_trains WHERE number = :number")
    fun isTrainSaved(number: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedLiveStatus(status: CachedLiveStatusEntity)

    @Query("SELECT * FROM cached_live_statuses WHERE trainNumber = :number")
    suspend fun getCachedLiveStatus(number: String): CachedLiveStatusEntity?
}
