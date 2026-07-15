package find.my.train.domain.model

data class Station(
    val code: String,
    val name: String,
    val arrivalTime: String? = null,
    val departureTime: String? = null,
    val haltTimeMinutes: Int = 0,
    val distanceKm: Int = 0,
    val day: Int = 1,
    val platform: String? = null,
    val isPassed: Boolean = false,
    val actualArrivalTime: String? = null,
    val actualDepartureTime: String? = null
)
