package find.my.train.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_trains")
data class SavedTrainEntity(
    @PrimaryKey val number: String,
    val name: String,
    val source: String,
    val destination: String,
    val sourceCode: String,
    val destinationCode: String,
    val departureTime: String,
    val arrivalTime: String,
    val runningDaysJson: String, // Comma-separated
    val trainType: String,
    val travelTime: String,
    val classesJson: String,     // Comma-separated
    val savedAt: Long
)
