package find.my.train.domain.usecase

import find.my.train.domain.model.PnrStatus
import find.my.train.domain.repository.TrainRepository
import javax.inject.Inject

class GetPnrStatusUseCase @Inject constructor(
    private val repository: TrainRepository
) {
    suspend operator fun invoke(pnrNumber: String, isOnline: Boolean): Result<PnrStatus> {
        return try {
            val trimmed = pnrNumber.trim()
            if (trimmed.length != 10 || !trimmed.all { it.isDigit() }) {
                Result.failure(IllegalArgumentException("PNR number must be exactly 10 digits."))
            } else {
                Result.success(repository.getPnrStatus(trimmed, isOnline))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
