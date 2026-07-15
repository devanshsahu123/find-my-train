package find.my.train.data.remote.model

import com.google.gson.annotations.SerializedName

data class TrainResponse(
    @SerializedName("number") val number: String,
    @SerializedName("name") val name: String,
    @SerializedName("source") val source: String,
    @SerializedName("destination") val destination: String,
    @SerializedName("source_code") val sourceCode: String,
    @SerializedName("destination_code") val destinationCode: String,
    @SerializedName("departure_time") val departureTime: String,
    @SerializedName("arrival_time") val arrivalTime: String,
    @SerializedName("running_days") val runningDays: List<String>,
    @SerializedName("train_type") val trainType: String,
    @SerializedName("travel_time") val travelTime: String,
    @SerializedName("classes") val classes: List<String>
)

data class StationResponse(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("arrival_time") val arrivalTime: String? = null,
    @SerializedName("departure_time") val departureTime: String? = null,
    @SerializedName("halt_time_minutes") val haltTimeMinutes: Int = 0,
    @SerializedName("distance_km") val distanceKm: Int = 0,
    @SerializedName("day") val day: Int = 1,
    @SerializedName("platform") val platform: String? = null,
    @SerializedName("is_passed") val isPassed: Boolean = false,
    @SerializedName("actual_arrival_time") val actualArrivalTime: String? = null,
    @SerializedName("actual_departure_time") val actualDepartureTime: String? = null
)

data class LiveStatusResponse(
    @SerializedName("train_number") val trainNumber: String,
    @SerializedName("train_name") val trainName: String,
    @SerializedName("current_station_name") val currentStationName: String,
    @SerializedName("current_station_code") val currentStationCode: String,
    @SerializedName("delay_minutes") val delayMinutes: Int,
    @SerializedName("speed_kmh") val speedKmh: Int,
    @SerializedName("platform") val platform: String,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("last_station_name") val lastStationName: String,
    @SerializedName("next_station_name") val nextStationName: String,
    @SerializedName("route_progress") val routeProgress: Float,
    @SerializedName("route_stations") val routeStations: List<StationResponse>,
    @SerializedName("status_message") val statusMessage: String
)

data class PnrResponse(
    @SerializedName("pnr_number") val pnrNumber: String,
    @SerializedName("train_number") val trainNumber: String,
    @SerializedName("train_name") val trainName: String,
    @SerializedName("journey_date") val journeyDate: String,
    @SerializedName("source_code") val sourceCode: String,
    @SerializedName("destination_code") val destinationCode: String,
    @SerializedName("source_name") val sourceName: String,
    @SerializedName("destination_name") val destinationName: String,
    @SerializedName("boarding_station") val boardingStation: String,
    @SerializedName("reservation_class") val reservationClass: String,
    @SerializedName("chart_status") val chartStatus: String,
    @SerializedName("passengers") val passengers: List<PassengerResponse>
)

data class PassengerResponse(
    @SerializedName("passenger_no") val passengerNo: Int,
    @SerializedName("booking_status") val bookingStatus: String,
    @SerializedName("current_status") val currentStatus: String,
    @SerializedName("coach") val coach: String,
    @SerializedName("berth_no") val berthNo: Int,
    @SerializedName("berth_type") val berthType: String
)

data class CoachLayoutResponse(
    @SerializedName("train_number") val trainNumber: String,
    @SerializedName("train_name") val trainName: String,
    @SerializedName("coaches") val coaches: List<CoachMetadataResponse>
)

data class CoachMetadataResponse(
    @SerializedName("label") val label: String,
    @SerializedName("type") val type: String,
    @SerializedName("position") val position: Int
)
