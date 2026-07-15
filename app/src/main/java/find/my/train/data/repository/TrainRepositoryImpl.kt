package find.my.train.data.repository

import com.google.gson.Gson
import find.my.train.data.local.dao.TrainDao
import find.my.train.data.local.entity.CachedLiveStatusEntity
import find.my.train.data.local.entity.SavedTrainEntity
import find.my.train.data.mock.MockData
import find.my.train.data.remote.TrainApiService
import find.my.train.data.remote.model.*
import find.my.train.domain.model.*
import find.my.train.domain.repository.TrainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainRepositoryImpl @Inject constructor(
    private val apiService: TrainApiService,
    private val trainDao: TrainDao
) : TrainRepository {

    private val gson = Gson()

    override suspend fun searchTrains(query: String, isOnline: Boolean): List<Train> {
        if (isOnline) {
            try {
                val remoteList = apiService.searchTrains(query)
                if (remoteList.isNotEmpty()) {
                    return remoteList.map { it.toDomain() }
                }
            } catch (e: Exception) {
                // Fallback
            }
        }
        delay(500)
        val filtered = MockData.trainsList.filter {
            it.number.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
        }
        return filtered.map { it.toDomain() }
    }

    override fun getLiveStatus(number: String, isOnline: Boolean): Flow<LiveStatus> = flow {
        if (isOnline) {
            while (true) {
                try {
                    val remoteStatus = try {
                        apiService.getLiveStatus(number)
                    } catch (e: Exception) {
                        MockData.getDynamicLiveStatus(number)
                    }
                    val domainStatus = remoteStatus.toDomain()

                    // Save to local cache
                    val json = gson.toJson(remoteStatus)
                    trainDao.insertCachedLiveStatus(
                        CachedLiveStatusEntity(
                            trainNumber = number,
                            cachedJson = json,
                            lastUpdated = System.currentTimeMillis()
                        )
                    )
                    emit(domainStatus)
                } catch (e: Exception) {
                    val cached = trainDao.getCachedLiveStatus(number)
                    if (cached != null) {
                        val response = gson.fromJson(cached.cachedJson, LiveStatusResponse::class.java)
                        emit(response.toDomain().copy(statusMessage = "Offline Fallback - ${response.statusMessage}"))
                    } else {
                        throw e
                    }
                }
                delay(5000) // Stream updates every 5 seconds
            }
        } else {
            val cached = trainDao.getCachedLiveStatus(number)
            if (cached != null) {
                val response = gson.fromJson(cached.cachedJson, LiveStatusResponse::class.java)
                val relativeTime = getRelativeTimeSpan(cached.lastUpdated)
                emit(response.toDomain().copy(
                    statusMessage = "Offline Mode - Cache updated $relativeTime"
                ))
            } else {
                throw IOException("No offline tracking data cached for train $number. Search this train while online first to cache its live data.")
            }
        }
    }

    private fun calculateDuration(depTime: String, arrTime: String): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val depDate = sdf.parse(depTime) ?: return "1h 00m"
            val arrDate = sdf.parse(arrTime) ?: return "1h 00m"
            
            var diffMs = arrDate.time - depDate.time
            if (diffMs < 0) {
                // Next day arrival
                diffMs += 24 * 60 * 60 * 1000
            }
            val diffMins = diffMs / (60 * 1000)
            val hours = diffMins / 60
            val mins = diffMins % 60
            "${hours}h ${mins}m"
        } catch (e: Exception) {
            "1h 00m"
        }
    }

    private fun adjustTrainTimings(trains: List<Train>, source: String, destination: String): List<Train> {
        return trains.map { train ->
            val route = MockData.trainSchedules[train.number] ?: return@map train
            val sourceStation = route.find { it.code.equals(source, ignoreCase = true) || it.name.contains(source, ignoreCase = true) }
            val destStation = route.find { it.code.equals(destination, ignoreCase = true) || it.name.contains(destination, ignoreCase = true) }
            
            if (sourceStation != null && destStation != null) {
                val sourceIndex = route.indexOf(sourceStation)
                val destIndex = route.indexOf(destStation)
                if (sourceIndex < destIndex) {
                    val depTime = sourceStation.departureTime ?: sourceStation.arrivalTime ?: "00:00"
                    val arrTime = destStation.arrivalTime ?: destStation.departureTime ?: "00:00"
                    val durationStr = calculateDuration(depTime, arrTime)
                    
                    train.copy(
                        sourceCode = sourceStation.code,
                        source = sourceStation.name,
                        departureTime = depTime,
                        destinationCode = destStation.code,
                        destination = destStation.name,
                        arrivalTime = arrTime,
                        travelTime = durationStr
                    )
                } else {
                    train
                }
            } else {
                train
            }
        }
    }

    override suspend fun getTrainsBetweenStations(
        source: String,
        destination: String,
        date: String,
        isOnline: Boolean
    ): List<Train> {
        if (isOnline) {
            try {
                val remoteList = apiService.getTrainsBetweenStations(source, destination, date)
                if (remoteList.isNotEmpty()) {
                    val domainList = remoteList.map { it.toDomain() }
                    return adjustTrainTimings(domainList, source, destination)
                }
            } catch (e: Exception) {
                // Fallback
            }
        }
        delay(600)
        val domainTrains = MockData.trainsList.filter { train ->
            val route = MockData.trainSchedules[train.number] ?: emptyList()
            val hasSource = route.any { it.code.equals(source, ignoreCase = true) || it.name.contains(source, ignoreCase = true) }
            val hasDest = route.any { it.code.equals(destination, ignoreCase = true) || it.name.contains(destination, ignoreCase = true) }
            hasSource && hasDest
        }.map { it.toDomain() }
        
        return adjustTrainTimings(domainTrains, source, destination)
    }

    override suspend fun getPnrStatus(pnrNumber: String, isOnline: Boolean): PnrStatus {
        if (isOnline) {
            try {
                return apiService.getPnrStatus(pnrNumber).toDomain()
            } catch (e: Exception) {
                // Fallback
            }
        }
        delay(500)
        val response = MockData.pnrList.firstOrNull { it.pnrNumber == pnrNumber }
            ?: throw IOException("No records found for PNR $pnrNumber. Try 4234567890 or 9876543210.")
        return response.toDomain()
    }

    override suspend fun getCoachPosition(number: String, isOnline: Boolean): CoachLayout {
        if (isOnline) {
            try {
                return apiService.getCoachPosition(number).toDomain()
            } catch (e: Exception) {
                // Fallback
            }
        }
        delay(400)
        val response = MockData.coachPositions[number]
            ?: throw IOException("Coach layout composition not found for Train $number.")
        return response.toDomain()
    }

    override fun getSavedTrains(): Flow<List<Train>> {
        return trainDao.getSavedTrains().map { list ->
            list.map { entity ->
                Train(
                    number = entity.number,
                    name = entity.name,
                    source = entity.source,
                    destination = entity.destination,
                    sourceCode = entity.sourceCode,
                    destinationCode = entity.destinationCode,
                    departureTime = entity.departureTime,
                    arrivalTime = entity.arrivalTime,
                    runningDays = entity.runningDaysJson.split(",").filter { it.isNotEmpty() },
                    trainType = entity.trainType,
                    travelTime = entity.travelTime,
                    classes = entity.classesJson.split(",").filter { it.isNotEmpty() },
                    isSaved = true
                )
            }
        }
    }

    override suspend fun saveTrain(train: Train) {
        trainDao.insertSavedTrain(
            SavedTrainEntity(
                number = train.number,
                name = train.name,
                source = train.source,
                destination = train.destination,
                sourceCode = train.sourceCode,
                destinationCode = train.destinationCode,
                departureTime = train.departureTime,
                arrivalTime = train.arrivalTime,
                runningDaysJson = train.runningDays.joinToString(","),
                trainType = train.trainType,
                travelTime = train.travelTime,
                classesJson = train.classes.joinToString(","),
                savedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteTrain(number: String) {
        trainDao.deleteSavedTrain(number)
    }

    override fun isTrainSaved(number: String): Flow<Boolean> {
        return trainDao.isTrainSaved(number)
    }

    private fun getRelativeTimeSpan(time: Long): String {
        val diff = System.currentTimeMillis() - time
        val seconds = diff / 1000
        val minutes = seconds / 60
        return when {
            minutes < 1 -> "just now"
            minutes == 1L -> "1 minute ago"
            else -> "$minutes minutes ago"
        }
    }

    // Mapper Extensions
    private fun TrainResponse.toDomain() = Train(
        number = number,
        name = name,
        source = source,
        destination = destination,
        sourceCode = sourceCode,
        destinationCode = destinationCode,
        departureTime = departureTime,
        arrivalTime = arrivalTime,
        runningDays = runningDays,
        trainType = trainType,
        travelTime = travelTime,
        classes = classes
    )

    private fun StationResponse.toDomain() = Station(
        code = code,
        name = name,
        arrivalTime = arrivalTime,
        departureTime = departureTime,
        haltTimeMinutes = haltTimeMinutes,
        distanceKm = distanceKm,
        day = day,
        platform = platform,
        isPassed = isPassed,
        actualArrivalTime = actualArrivalTime,
        actualDepartureTime = actualDepartureTime
    )

    private fun LiveStatusResponse.toDomain() = LiveStatus(
        trainNumber = trainNumber,
        trainName = trainName,
        currentStationName = currentStationName,
        currentStationCode = currentStationCode,
        delayMinutes = delayMinutes,
        speedKmh = speedKmh,
        platform = platform,
        lastUpdated = lastUpdated,
        lastStationName = lastStationName,
        nextStationName = nextStationName,
        routeProgress = routeProgress,
        routeStations = routeStations.map { it.toDomain() },
        statusMessage = statusMessage
    )

    private fun PassengerResponse.toDomain() = Passenger(
        passengerNo = passengerNo,
        bookingStatus = bookingStatus,
        currentStatus = currentStatus,
        coach = coach,
        berthNo = berthNo,
        berthType = berthType
    )

    private fun PnrResponse.toDomain() = PnrStatus(
        pnrNumber = pnrNumber,
        trainNumber = trainNumber,
        trainName = trainName,
        journeyDate = journeyDate,
        sourceCode = sourceCode,
        destinationCode = destinationCode,
        sourceName = sourceName,
        destinationName = destinationName,
        boardingStation = boardingStation,
        reservationClass = reservationClass,
        chartStatus = chartStatus,
        passengers = passengers.map { it.toDomain() }
    )

    private fun CoachMetadataResponse.toDomain() = CoachMetadata(
        label = label,
        type = type,
        position = position
    )

    private fun CoachLayoutResponse.toDomain() = CoachLayout(
        trainNumber = trainNumber,
        trainName = trainName,
        coaches = coaches.map { it.toDomain() }
    )
}
