package find.my.train.ui.coach

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.domain.model.CoachLayout
import find.my.train.domain.repository.TrainRepository
import find.my.train.util.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CoachUiState {
    object Loading : CoachUiState
    data class Success(val coachLayout: CoachLayout) : CoachUiState
    data class Error(val message: String) : CoachUiState
}

@HiltViewModel
class CoachPositionViewModel @Inject constructor(
    private val repository: TrainRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val trainNumber: String = checkNotNull(savedStateHandle["trainNumber"])

    private val _uiState = MutableStateFlow<CoachUiState>(CoachUiState.Loading)
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    init {
        fetchCoachLayout()
    }

    fun fetchCoachLayout() {
        viewModelScope.launch {
            _uiState.value = CoachUiState.Loading
            try {
                val connected = networkMonitor.isCurrentlyConnected()
                val layout = repository.getCoachPosition(trainNumber, connected)
                _uiState.value = CoachUiState.Success(layout)
            } catch (e: Exception) {
                _uiState.value = CoachUiState.Error(e.localizedMessage ?: "Failed to load coach layout.")
            }
        }
    }
}
