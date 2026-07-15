package find.my.train.domain.usecase

import find.my.train.domain.model.Train
import find.my.train.domain.repository.TrainRepository
import javax.inject.Inject

class GetTrainsBetweenStationsUseCase @Inject constructor(
    private val repository: TrainRepository
) {
    suspend operator fun invoke(
        source: String,
        destination: String,
        date: String,
        isOnline: Boolean
    ): Result<List<Train>> {
        return try {
            if (source.trim().isEmpty() || destination.trim().isEmpty()) {
                Result.failure(IllegalArgumentException("Source and destination stations cannot be empty."))
            } else {
                Result.success(repository.getTrainsBetweenStations(source.trim(), destination.trim(), date, isOnline))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
