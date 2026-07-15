package find.my.train.data.mock

import find.my.train.data.remote.model.*
import java.text.SimpleDateFormat
import java.util.*

object MockData {

    val stationsList = listOf(
        StationResponse("RKMP", "Rani Kamalapati", null, null, 0, 0, 1, null, false),
        StationResponse("RQJ", "Raninagar Jalpaiguri Junction", null, null, 0, 0, 1, null, false),
        StationResponse("RANI", "Rani", null, null, 0, 0, 1, null, false),
        StationResponse("RNR", "Ranibennur", null, null, 0, 0, 1, null, false),
        StationResponse("RNG", "RaniGanj", null, null, 0, 0, 1, null, false),
        StationResponse("NDPM", "Narmadapuram", null, null, 0, 0, 1, null, false),
        StationResponse("SDLP", "Sadulpur", null, null, 0, 0, 1, null, false),
        StationResponse("NDLS", "New Delhi", null, null, 0, 0, 1, null, false),
        StationResponse("BPL", "Bhopal Jn", null, null, 0, 0, 1, null, false),
        StationResponse("BSB", "Varanasi Jn", null, null, 0, 0, 1, null, false),
        StationResponse("MTJ", "Mathura Jn", null, null, 0, 0, 1, null, false),
        StationResponse("AGC", "Agra Cantt", null, null, 0, 0, 1, null, false),
        StationResponse("GWL", "Gwalior Jn", null, null, 0, 0, 1, null, false),
        StationResponse("VGLJ", "VGL Jhansi Jn", null, null, 0, 0, 1, null, false),
        StationResponse("BINA", "Bina Jn", null, null, 0, 0, 1, null, false),
        StationResponse("KOTA", "Kota Jn", null, null, 0, 0, 1, null, false),
        StationResponse("RTM", "Ratlam Jn", null, null, 0, 0, 1, null, false),
        StationResponse("BRC", "Vadodara Jn", null, null, 0, 0, 1, null, false),
        StationResponse("ST", "Surat", null, null, 0, 0, 1, null, false),
        StationResponse("MMCT", "Mumbai Central", null, null, 0, 0, 1, null, false)
    )

    val trainsList = listOf(
        TrainResponse(
            number = "22436",
            name = "Vande Bharat Express",
            source = "New Delhi (NDLS)",
            destination = "Varanasi Jn (BSB)",
            sourceCode = "NDLS",
            destinationCode = "BSB",
            departureTime = "06:00",
            arrivalTime = "14:00",
            runningDays = listOf("Mon", "Tue", "Wed", "Fri", "Sat", "Sun"),
            trainType = "Vande Bharat",
            travelTime = "8h 00m",
            classes = listOf("CC", "EC")
        ),
        TrainResponse(
            number = "12002",
            name = "NDLS-RKMP Shatabdi Express",
            source = "New Delhi (NDLS)",
            destination = "Rani Kamalapati (RKMP)",
            sourceCode = "NDLS",
            destinationCode = "RKMP",
            departureTime = "06:00",
            arrivalTime = "14:40",
            runningDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            trainType = "Shatabdi",
            travelTime = "8h 40m",
            classes = listOf("CC", "EC")
        ),
        TrainResponse(
            number = "12424",
            name = "New Delhi Dibrugarh Town Rajdhani",
            source = "New Delhi (NDLS)",
            destination = "Dibrugarh Town (DBRT)",
            sourceCode = "NDLS",
            destinationCode = "DBRT",
            departureTime = "16:10",
            arrivalTime = "07:00",
            runningDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            trainType = "Rajdhani",
            travelTime = "38h 50m",
            classes = listOf("3A", "2A", "1A")
        ),
        TrainResponse(
            number = "12952",
            name = "Mumbai Central Rajdhani Express",
            source = "New Delhi (NDLS)",
            destination = "Mumbai Central (MMCT)",
            sourceCode = "NDLS",
            destinationCode = "MMCT",
            departureTime = "16:55",
            arrivalTime = "08:35",
            runningDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            trainType = "Rajdhani",
            travelTime = "15h 40m",
            classes = listOf("3A", "2A", "1A")
        ),
        TrainResponse(
            number = "12626",
            name = "Kerala Express",
            source = "New Delhi (NDLS)",
            destination = "Thiruvananthapuram Central (TVC)",
            sourceCode = "NDLS",
            destinationCode = "TVC",
            departureTime = "20:10",
            arrivalTime = "21:50",
            runningDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            trainType = "Mail Express",
            travelTime = "49h 40m",
            classes = listOf("SL", "3A", "2A", "1A")
        ),
        TrainResponse(
            number = "12056",
            name = "Dehradun NDLS Jan Shatabdi",
            source = "Dehradun (DDN)",
            destination = "New Delhi (NDLS)",
            sourceCode = "DDN",
            destinationCode = "NDLS",
            departureTime = "05:00",
            arrivalTime = "11:15",
            runningDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            trainType = "Jan Shatabdi",
            travelTime = "6h 15m",
            classes = listOf("2S", "CC")
        ),
        TrainResponse(
            number = "12854",
            name = "Amarkantak Express",
            source = "Rani Kamalapati (RKMP)",
            destination = "Narmadapuram (NDPM)",
            sourceCode = "RKMP",
            destinationCode = "NDPM",
            departureTime = "15:40",
            arrivalTime = "16:45",
            runningDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            trainType = "Mail Express",
            travelTime = "1h 05m",
            classes = listOf("SL", "3A", "2A")
        ),
        TrainResponse(
            number = "17020",
            name = "Hisar Weekly Express",
            source = "Bhopal Jn (BPL)",
            destination = "Sadulpur (SDLP)",
            sourceCode = "BPL",
            destinationCode = "SDLP",
            departureTime = "19:15",
            arrivalTime = "08:45",
            runningDays = listOf("Sun"),
            trainType = "Mail Express",
            travelTime = "13h 30m",
            classes = listOf("SL", "3A", "2A")
        ),
        TrainResponse(
            number = "11464",
            name = "Veraval Express",
            source = "Narmadapuram (NDPM)",
            destination = "Rani Kamalapati (RKMP)",
            sourceCode = "NDPM",
            destinationCode = "RKMP",
            departureTime = "12:30",
            arrivalTime = "13:45",
            runningDays = listOf("Mon", "Wed", "Thu", "Fri", "Sat"),
            trainType = "Mail Express",
            travelTime = "1h 15m",
            classes = listOf("SL", "3A")
        ),
        TrainResponse(
            number = "20843",
            name = "Bhagat Ki Kothi SF Express",
            source = "Narmadapuram (NDPM)",
            destination = "Rani Kamalapati (RKMP)",
            sourceCode = "NDPM",
            destinationCode = "RKMP",
            departureTime = "09:10",
            arrivalTime = "10:20",
            runningDays = listOf("Tue", "Sat"),
            trainType = "Superfast",
            travelTime = "1h 10m",
            classes = listOf("SL", "3A", "2A")
        ),
        TrainResponse(
            number = "12615",
            name = "Grand Trunk Express",
            source = "Narmadapuram (NDPM)",
            destination = "Rani Kamalapati (RKMP)",
            sourceCode = "NDPM",
            destinationCode = "RKMP",
            departureTime = "18:20",
            arrivalTime = "19:30",
            runningDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            trainType = "Superfast",
            travelTime = "1h 10m",
            classes = listOf("SL", "3A", "2A", "1A")
        )
    )

    val trainSchedules = mapOf(
        "22436" to listOf(
            StationResponse("NDLS", "New Delhi", null, "06:00", 0, 0, 1, "16", true),
            StationResponse("CNB", "Kanpur Central", "10:15", "10:17", 2, 440, 1, "1", false),
            StationResponse("PRYJ", "Prayagraj Jn", "12:20", "12:22", 2, 634, 1, "6", false),
            StationResponse("BSB", "Varanasi Jn", "14:00", null, 0, 755, 1, "1", false)
        ),
        "12002" to listOf(
            StationResponse("NDLS", "New Delhi", null, "06:00", 0, 0, 1, "1", true),
            StationResponse("MTJ", "Mathura Jn", "07:19", "07:20", 1, 141, 1, "1", false),
            StationResponse("AGC", "Agra Cantt", "07:50", "07:55", 5, 195, 1, "1", false),
            StationResponse("GWL", "Gwalior Jn", "09:23", "09:28", 5, 313, 1, "1", false),
            StationResponse("VGLJ", "VGL Jhansi Jn", "10:40", "10:45", 5, 411, 1, "1", false),
            StationResponse("BINA", "Bina Jn", "12:40", "12:42", 2, 564, 1, "3", false),
            StationResponse("BPL", "Bhopal Jn", "14:12", "14:15", 3, 703, 1, "1", false),
            StationResponse("RKMP", "Rani Kamalapati", "14:40", null, 0, 709, 1, "5", false)
        ),
        "12952" to listOf(
            StationResponse("NDLS", "New Delhi", null, "16:55", 0, 0, 1, "3", true),
            StationResponse("KOTA", "Kota Jn", "22:15", "22:25", 10, 465, 1, "1", false),
            StationResponse("RTM", "Ratlam Jn", "01:50", "01:53", 3, 731, 2, "4", false),
            StationResponse("BRC", "Vadodara Jn", "04:50", "05:00", 10, 992, 2, "1", false),
            StationResponse("ST", "Surat", "06:37", "06:42", 5, 1122, 2, "2", false),
            StationResponse("MMCT", "Mumbai Central", "08:35", null, 0, 1385, 2, "1", false)
        ),
        "12854" to listOf(
            StationResponse("RKMP", "Rani Kamalapati", null, "15:40", 0, 0, 1, "1", true),
            StationResponse("NDPM", "Narmadapuram", "16:45", null, 0, 75, 1, "2", false)
        ),
        "17020" to listOf(
            StationResponse("BPL", "Bhopal Jn", null, "19:15", 0, 0, 1, "1", true),
            StationResponse("SDLP", "Sadulpur", "08:45", null, 0, 850, 2, "3", false)
        ),
        "11464" to listOf(
            StationResponse("NDPM", "Narmadapuram", null, "12:30", 0, 0, 1, "1", true),
            StationResponse("RKMP", "Rani Kamalapati", "13:45", null, 0, 75, 1, "2", false)
        ),
        "20843" to listOf(
            StationResponse("NDPM", "Narmadapuram", null, "09:10", 0, 0, 1, "1", true),
            StationResponse("RKMP", "Rani Kamalapati", "10:20", null, 0, 75, 1, "2", false)
        ),
        "12615" to listOf(
            StationResponse("NDPM", "Narmadapuram", null, "18:20", 0, 0, 1, "1", true),
            StationResponse("RKMP", "Rani Kamalapati", "19:30", null, 0, 75, 1, "2", false)
        )
    )

    val pnrList = listOf(
        PnrResponse(
            pnrNumber = "4234567890",
            trainNumber = "22436",
            trainName = "Vande Bharat Express",
            journeyDate = "18-Jul-2026",
            sourceCode = "NDLS",
            destinationCode = "BSB",
            sourceName = "New Delhi",
            destinationName = "Varanasi Jn",
            boardingStation = "New Delhi (NDLS)",
            reservationClass = "AC Chair Car (CC)",
            chartStatus = "CHART PREPARED",
            passengers = listOf(
                PassengerResponse(1, "CNF/C1/12", "CNF/C1/12", "C1", 12, "WS"),
                PassengerResponse(2, "CNF/C1/15", "CNF/C1/15", "C1", 15, "LB")
            )
        ),
        PnrResponse(
            pnrNumber = "9876543210",
            trainNumber = "12952",
            trainName = "Mumbai Central Rajdhani Express",
            journeyDate = "22-Jul-2026",
            sourceCode = "NDLS",
            destinationCode = "MMCT",
            sourceName = "New Delhi",
            destinationName = "Mumbai Central",
            boardingStation = "New Delhi (NDLS)",
            reservationClass = "AC 3 Tier (3A)",
            chartStatus = "CHART NOT PREPARED",
            passengers = listOf(
                PassengerResponse(1, "RAC/B2/24", "CNF/B2/24", "B2", 24, "SL"),
                PassengerResponse(2, "WL 3", "RAC 1", "B2", 25, "SU")
            )
        )
    )

    val coachPositions = mapOf(
        "22436" to CoachLayoutResponse(
            trainNumber = "22436",
            trainName = "Vande Bharat Express",
            coaches = listOf(
                CoachMetadataResponse("LOCO", "Engine", 1),
                CoachMetadataResponse("C1", "AC Chair Car", 2),
                CoachMetadataResponse("C2", "AC Chair Car", 3),
                CoachMetadataResponse("C3", "AC Chair Car", 4),
                CoachMetadataResponse("E1", "Executive Class", 5),
                CoachMetadataResponse("E2", "Executive Class", 6),
                CoachMetadataResponse("C4", "AC Chair Car", 7),
                CoachMetadataResponse("C5", "AC Chair Car", 8),
                CoachMetadataResponse("C6", "AC Chair Car", 9),
                CoachMetadataResponse("EOG", "Generator", 10)
            )
        ),
        "12002" to CoachLayoutResponse(
            trainNumber = "12002",
            trainName = "NDLS-RKMP Shatabdi Express",
            coaches = listOf(
                CoachMetadataResponse("LOCO", "Engine", 1),
                CoachMetadataResponse("EOG", "Generator", 2),
                CoachMetadataResponse("C1", "AC Chair Car", 3),
                CoachMetadataResponse("C2", "AC Chair Car", 4),
                CoachMetadataResponse("C3", "AC Chair Car", 5),
                CoachMetadataResponse("C4", "AC Chair Car", 6),
                CoachMetadataResponse("C5", "AC Chair Car", 7),
                CoachMetadataResponse("C6", "AC Chair Car", 8),
                CoachMetadataResponse("E1", "Executive Class", 9),
                CoachMetadataResponse("EOG", "Generator", 10)
            )
        ),
        "12952" to CoachLayoutResponse(
            trainNumber = "12952",
            trainName = "Mumbai Central Rajdhani Express",
            coaches = listOf(
                CoachMetadataResponse("LOCO", "Engine", 1),
                CoachMetadataResponse("EOG", "Generator", 2),
                CoachMetadataResponse("B1", "AC 3 Tier", 3),
                CoachMetadataResponse("B2", "AC 3 Tier", 4),
                CoachMetadataResponse("B3", "AC 3 Tier", 5),
                CoachMetadataResponse("A1", "AC 2 Tier", 6),
                CoachMetadataResponse("A2", "AC 2 Tier", 7),
                CoachMetadataResponse("H1", "AC 1st Class", 8),
                CoachMetadataResponse("PC", "Pantry Car", 9),
                CoachMetadataResponse("EOG", "Generator", 10)
            )
        )
    )

    /**
     * Generates a stateless, dynamic real-time live status for a train.
     * The position, delay, speed, and status messages update every 10 seconds based on system clock,
     * simulating a live train feed.
     */
    fun getDynamicLiveStatus(trainNumber: String): LiveStatusResponse {
        val train = trainsList.firstOrNull { it.number == trainNumber } ?: TrainResponse(
            number = trainNumber,
            name = "Express Train",
            source = "Source Station",
            destination = "Destination Station",
            sourceCode = "SRC",
            destinationCode = "DEST",
            departureTime = "12:00",
            arrivalTime = "18:00",
            runningDays = listOf("Daily"),
            trainType = "Mail Express",
            travelTime = "6h 00m",
            classes = listOf("SL", "3A", "2A")
        )

        val route = trainSchedules[trainNumber] ?: listOf(
            StationResponse(train.sourceCode, train.source.substringBefore(" ("), null, train.departureTime, 0, 0, 1, "1", true),
            StationResponse(train.destinationCode, train.destination.substringBefore(" ("), train.arrivalTime, null, 0, 500, 1, "2", false)
        )

        val totalStations = route.size
        
        // Use system epoch to calculate a dynamic station index (transitions every 15 seconds)
        val cycleSeconds = 15
        val epochSeconds = System.currentTimeMillis() / 1000
        val rawIndex = ((epochSeconds / cycleSeconds) % (totalStations + 1)).toInt()
        
        // rawIndex = 0: Train not started
        // rawIndex = 1 to totalStations-1: Train between stations / at stations
        // rawIndex = totalStations: Train arrived at destination
        
        val currentIndex: Int
        val isAtStation: Boolean
        
        if (rawIndex == 0) {
            currentIndex = 0
            isAtStation = true
        } else if (rawIndex >= totalStations) {
            currentIndex = totalStations - 1
            isAtStation = true
        } else {
            currentIndex = rawIndex
            // Use time to toggle between running (between stations) and halted (at station)
            isAtStation = (epochSeconds % cycleSeconds) < 6
        }

        // Speed calculation based on whether train is running or halted
        val baseSpeed = if (train.trainType == "Vande Bharat") 110 else if (train.trainType == "Rajdhani") 100 else 75
        val currentSpeed = if (isAtStation) 0 else (baseSpeed + (epochSeconds % 15).toInt())

        // Delay calculation: stable 0 delay to match scheduled times perfectly
        val delay = 0

        val currentStation = route[currentIndex]
        val lastStation = route[if (currentIndex > 0) currentIndex - 1 else 0]
        val nextStation = route[if (currentIndex < totalStations - 1) currentIndex + 1 else totalStations - 1]

        // Map route stations to their passed status
        val updatedStations = route.mapIndexed { index, station ->
            val passed = index < currentIndex || (index == currentIndex && isAtStation)
            val schedArrival = station.arrivalTime ?: station.departureTime ?: "--:--"
            
            // Calculate a simulated actual time with the delay added
            val actualArr = if (station.arrivalTime != null) addMinutesToTime(station.arrivalTime, delay) else null
            val actualDep = if (station.departureTime != null) addMinutesToTime(station.departureTime, delay) else null

            station.copy(
                isPassed = passed,
                actualArrivalTime = actualArr,
                actualDepartureTime = actualDep,
                platform = station.platform ?: ((index % 3) + 1).toString()
            )
        }

        // Calculate progress percentage
        val progress = if (currentIndex == totalStations - 1 && isAtStation) {
            1.0f
        } else {
            val segmentPercent = if (isAtStation) 0.0f else 0.5f
            (currentIndex.toFloat() + segmentPercent) / (totalStations - 1).toFloat()
        }

        val lastUpdatedFormatted = SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH).format(Date())

        val statusMsg = when {
            currentIndex == 0 && isAtStation -> "Scheduled to depart from ${currentStation.name} at ${train.departureTime}. Platform ${currentStation.platform}."
            currentIndex == totalStations - 1 && isAtStation -> "Arrived at ${currentStation.name} at ${currentStation.actualArrivalTime ?: train.arrivalTime}. Delay of $delay mins."
            isAtStation -> "Halted at ${currentStation.name}. Platform ${currentStation.platform}. Delay of $delay mins. Departure expected in ${currentStation.haltTimeMinutes} mins."
            else -> "Running between ${lastStation.name} and ${nextStation.name} at $currentSpeed km/h. Expected arrival at ${nextStation.name} is ${addMinutesToTime(nextStation.arrivalTime ?: "12:00", delay)}."
        }

        return LiveStatusResponse(
            trainNumber = train.number,
            trainName = train.name,
            currentStationName = currentStation.name,
            currentStationCode = currentStation.code,
            delayMinutes = delay,
            speedKmh = currentSpeed,
            platform = currentStation.platform ?: "1",
            lastUpdated = lastUpdatedFormatted,
            lastStationName = lastStation.name,
            nextStationName = nextStation.name,
            routeProgress = progress.coerceIn(0.0f, 1.0f),
            routeStations = updatedStations,
            statusMessage = statusMsg
        )
    }

    private fun addMinutesToTime(timeStr: String, minutesToAdd: Int): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val date = sdf.parse(timeStr) ?: return timeStr
            val cal = Calendar.getInstance().apply {
                time = date
                add(Calendar.MINUTE, minutesToAdd)
            }
            sdf.format(cal.time)
        } catch (e: Exception) {
            timeStr
        }
    }
}
