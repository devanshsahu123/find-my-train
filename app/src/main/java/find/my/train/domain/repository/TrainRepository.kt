package find.my.train.domain.repository

import find.my.train.domain.model.*
import kotlinx.coroutines.flow.Flow

interface TrainRepository {
    suspend fun searchTrains(query: String, isOnline: Boolean): List<Train>
    
    fun getLiveStatus(number: String, isOnline: Boolean): Flow<LiveStatus>
    
    suspend fun getTrainsBetweenStations(
        source: String, 
        destination: String, 
        date: String, 
        isOnline: Boolean
    ): List<Train>
    
    suspend fun getPnrStatus(pnrNumber: String, isOnline: Boolean): PnrStatus
    
    suspend fun getCoachPosition(number: String, isOnline: Boolean): CoachLayout
    
    fun getSavedTrains(): Flow<List<Train>>
    
    suspend fun saveTrain(train: Train)
    
    suspend fun deleteTrain(number: String)
    
    fun isTrainSaved(number: String): Flow<Boolean>
}
