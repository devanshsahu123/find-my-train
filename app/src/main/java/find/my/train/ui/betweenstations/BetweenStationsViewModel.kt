package find.my.train.ui.betweenstations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.domain.model.Train
import find.my.train.domain.usecase.GetSavedTrainsUseCase
import find.my.train.domain.usecase.GetTrainsBetweenStationsUseCase
import find.my.train.domain.usecase.SaveSearchHistoryUseCase
import find.my.train.domain.usecase.ToggleSaveTrainUseCase
import find.my.train.util.NetworkMonitor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

sealed interface BetweenStationsUiState {
    object Empty : BetweenStationsUiState
    object Loading : BetweenStationsUiState
    data class Success(val trains: List<Train>) : BetweenStationsUiState
    data class Error(val message: String) : BetweenStationsUiState
}

@HiltViewModel
class BetweenStationsViewModel @Inject constructor(
    private val getTrainsBetweenStationsUseCase: GetTrainsBetweenStationsUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase,
    private val toggleSaveTrainUseCase: ToggleSaveTrainUseCase,
    getSavedTrainsUseCase: GetSavedTrainsUseCase,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val savedTrainNumbers: StateFlow<Set<String>> = getSavedTrainsUseCase()
        .map { list -> list.map { it.number }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _source = MutableStateFlow("")
    val source = _source.asStateFlow()

    private val _destination = MutableStateFlow("")
    val destination = _destination.asStateFlow()

    private val _journeyDate = MutableStateFlow(
        SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(Date())
    )
    val journeyDate = _journeyDate.asStateFlow()

    private val _uiState = MutableStateFlow<BetweenStationsUiState>(BetweenStationsUiState.Empty)
    val uiState = _uiState.asStateFlow()

    init {
        val initialSource: String = savedStateHandle["source"] ?: ""
        val initialDest: String = savedStateHandle["destination"] ?: ""
        if (initialSource.isNotEmpty() && initialDest.isNotEmpty()) {
            _source.value = initialSource
            _destination.value = initialDest
            findTrains()
        }
    }

    fun onSourceChange(value: String) { _source.value = value }
    fun onDestinationChange(value: String) { _destination.value = value }
    fun onDateChange(value: String) { _journeyDate.value = value }

    fun swapStations() {
        val temp = _source.value
        _source.value = _destination.value
        _destination.value = temp
    }

    fun findTrains() {
        val src = _source.value.trim()
        val dest = _destination.value.trim()
        
        if (src.isEmpty() || dest.isEmpty()) {
            _uiState.value = BetweenStationsUiState.Error("Please enter both Source and Destination stations.")
            return
        }

        viewModelScope.launch {
            _uiState.value = BetweenStationsUiState.Loading
            getTrainsBetweenStationsUseCase(src, dest, _journeyDate.value, isOnline.value).fold(
                onSuccess = { trains ->
                    _uiState.value = BetweenStationsUiState.Success(trains)
                    if (trains.isNotEmpty()) {
                        saveSearchHistoryUseCase("$src to $dest", "STATIONS")
                    }
                },
                onFailure = { error ->
                    _uiState.value = BetweenStationsUiState.Error(error.localizedMessage ?: "Failed to find trains.")
                }
            )
        }
    }

    fun toggleSaveTrain(train: Train) {
        viewModelScope.launch {
            toggleSaveTrainUseCase(train)
        }
    }
}
