package find.my.train.domain.model

data class PnrStatus(
    val pnrNumber: String,
    val trainNumber: String,
    val trainName: String,
    val journeyDate: String,
    val sourceCode: String,
    val destinationCode: String,
    val sourceName: String,
    val destinationName: String,
    val boardingStation: String,
    val reservationClass: String,
    val chartStatus: String,
    val passengers: List<Passenger>
)

data class Passenger(
    val passengerNo: Int,
    val bookingStatus: String,
    val currentStatus: String,
    val coach: String,
    val berthNo: Int,
    val berthType: String // LB, MB, UB, SL, SU, etc.
)
