package find.my.train.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_live_statuses")
data class CachedLiveStatusEntity(
    @PrimaryKey val trainNumber: String,
    val cachedJson: String,
    val lastUpdated: Long
)
