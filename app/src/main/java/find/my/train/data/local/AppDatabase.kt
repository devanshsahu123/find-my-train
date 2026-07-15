package find.my.train.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import find.my.train.data.local.dao.SearchHistoryDao
import find.my.train.data.local.dao.TrainDao
import find.my.train.data.local.entity.CachedLiveStatusEntity
import find.my.train.data.local.entity.RecentSearchEntity
import find.my.train.data.local.entity.SavedTrainEntity

@Database(
    entities = [
        SavedTrainEntity::class,
        CachedLiveStatusEntity::class,
        RecentSearchEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trainDao(): TrainDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}
