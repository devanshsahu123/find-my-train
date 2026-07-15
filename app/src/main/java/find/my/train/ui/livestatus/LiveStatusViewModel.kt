package find.my.train.ui.livestatus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.domain.model.LiveStatus
import find.my.train.domain.model.Train
import find.my.train.domain.usecase.GetLiveStatusUseCase
import find.my.train.domain.usecase.IsTrainSavedUseCase
import find.my.train.domain.usecase.ToggleSaveTrainUseCase
import find.my.train.util.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LiveStatusUiState {
    object Loading : LiveStatusUiState
    data class Success(val status: LiveStatus) : LiveStatusUiState
    data class Error(val message: String) : LiveStatusUiState
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LiveStatusViewModel @Inject constructor(
    private val getLiveStatusUseCase: GetLiveStatusUseCase,
    private val isTrainSavedUseCase: IsTrainSavedUseCase,
    private val toggleSaveTrainUseCase: ToggleSaveTrainUseCase,
    networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val trainNumber: String = checkNotNull(savedStateHandle["trainNumber"])

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isSaved: StateFlow<Boolean> = isTrainSavedUseCase(trainNumber)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val uiState: StateFlow<LiveStatusUiState> = isOnline
        .flatMapLatest { online ->
            getLiveStatusUseCase(trainNumber, online)
                .map { LiveStatusUiState.Success(it) as LiveStatusUiState }
                .catch { emit(LiveStatusUiState.Error(it.localizedMessage ?: "Unknown error occurred.")) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LiveStatusUiState.Loading)

    fun toggleSave(train: Train) {
        viewModelScope.launch {
            toggleSaveTrainUseCase(train)
        }
    }
}
