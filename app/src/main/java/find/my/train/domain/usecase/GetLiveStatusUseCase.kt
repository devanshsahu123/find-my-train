package find.my.train.domain.usecase

import find.my.train.domain.model.LiveStatus
import find.my.train.domain.repository.TrainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLiveStatusUseCase @Inject constructor(
    private val repository: TrainRepository
) {
    operator fun invoke(number: String, isOnline: Boolean): Flow<LiveStatus> {
        return repository.getLiveStatus(number, isOnline)
    }
}
