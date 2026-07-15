package find.my.train.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val type: String, // "TRAIN" or "STATIONS"
    val timestamp: Long
)
