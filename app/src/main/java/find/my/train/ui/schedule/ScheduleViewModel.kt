package find.my.train.ui.schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.domain.model.Station
import find.my.train.domain.usecase.GetLiveStatusUseCase
import find.my.train.util.NetworkMonitor
import kotlinx.coroutines.flow.*
import javax.inject.Inject

sealed interface ScheduleUiState {
    object Loading : ScheduleUiState
    data class Success(val trainName: String, val stations: List<Station>) : ScheduleUiState
    data class Error(val message: String) : ScheduleUiState
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getLiveStatusUseCase: GetLiveStatusUseCase,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val trainNumber: String = checkNotNull(savedStateHandle["trainNumber"])

    val uiState: StateFlow<ScheduleUiState> = flow {
        emit(ScheduleUiState.Loading)
        try {
            val connected = networkMonitor.isCurrentlyConnected()
            getLiveStatusUseCase(trainNumber, connected).take(1).collect { status ->
                emit(ScheduleUiState.Success(status.trainName, status.routeStations))
            }
        } catch (e: Exception) {
            emit(ScheduleUiState.Error(e.localizedMessage ?: "Failed to load train schedule."))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScheduleUiState.Loading)
}
