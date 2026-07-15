package find.my.train.domain.model

data class LiveStatus(
    val trainNumber: String,
    val trainName: String,
    val currentStationName: String,
    val currentStationCode: String,
    val delayMinutes: Int,
    val speedKmh: Int,
    val platform: String,
    val lastUpdated: String,
    val lastStationName: String,
    val nextStationName: String,
    val routeProgress: Float,
    val routeStations: List<Station>,
    val statusMessage: String
)
