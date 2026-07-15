package find.my.train.domain.usecase

import find.my.train.domain.model.Train
import find.my.train.domain.repository.TrainRepository
import javax.inject.Inject

class SearchTrainsUseCase @Inject constructor(
    private val repository: TrainRepository
) {
    suspend operator fun invoke(query: String, isOnline: Boolean): Result<List<Train>> {
        return try {
            if (query.trim().isEmpty()) {
                Result.success(emptyList())
            } else {
                Result.success(repository.searchTrains(query.trim(), isOnline))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
