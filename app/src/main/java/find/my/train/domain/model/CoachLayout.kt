package find.my.train.domain.model

data class CoachLayout(
    val trainNumber: String,
    val trainName: String,
    val coaches: List<CoachMetadata>
)

data class CoachMetadata(
    val label: String,      // e.g. "C1", "S1", "A1", "LOCO", "EOG"
    val type: String,       // e.g. "Sleeper", "AC 3 Tier", "AC Chair Car", "Executive Class", "Engine", "Generator"
    val position: Int       // Sequence index from engine
)

data class SeatInfo(
    val seatNo: Int,
    val berthType: String,  // LB, MB, UB, SL, SU, WS (Window Seat), etc.
    val isBooked: Boolean
)
