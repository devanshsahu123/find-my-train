package find.my.train.domain.usecase

import find.my.train.domain.model.Train
import find.my.train.domain.repository.TrainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetSavedTrainsUseCase @Inject constructor(
    private val repository: TrainRepository
) {
    operator fun invoke(): Flow<List<Train>> = repository.getSavedTrains()
}

class ToggleSaveTrainUseCase @Inject constructor(
    private val repository: TrainRepository
) {
    suspend operator fun invoke(train: Train) {
        val isSaved = repository.isTrainSaved(train.number).first()
        if (isSaved) {
            repository.deleteTrain(train.number)
        } else {
            repository.saveTrain(train.copy(isSaved = true))
        }
    }
}

class IsTrainSavedUseCase @Inject constructor(
    private val repository: TrainRepository
) {
    operator fun invoke(number: String): Flow<Boolean> = repository.isTrainSaved(number)
}
