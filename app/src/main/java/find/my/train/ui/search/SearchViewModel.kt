package find.my.train.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.domain.model.Train
import find.my.train.domain.usecase.GetSavedTrainsUseCase
import find.my.train.domain.usecase.SearchTrainsUseCase
import find.my.train.domain.usecase.SaveSearchHistoryUseCase
import find.my.train.domain.usecase.ToggleSaveTrainUseCase
import find.my.train.util.NetworkMonitor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchUiState {
    object Empty : SearchUiState
    object Loading : SearchUiState
    data class Success(val trains: List<Train>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchTrainsUseCase: SearchTrainsUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase,
    private val toggleSaveTrainUseCase: ToggleSaveTrainUseCase,
    getSavedTrainsUseCase: GetSavedTrainsUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val savedTrainNumbers: StateFlow<Set<String>> = getSavedTrainsUseCase()
        .map { list -> list.map { it.number }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val uiState = _uiState.asStateFlow()

    init {
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.trim().isEmpty()) {
                    _uiState.value = SearchUiState.Empty
                } else {
                    performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            searchTrainsUseCase(query, isOnline.value).fold(
                onSuccess = { trains ->
                    _uiState.value = SearchUiState.Success(trains)
                    if (trains.isNotEmpty()) {
                        saveSearchHistoryUseCase(query, "TRAIN")
                    }
                },
                onFailure = { error ->
                    _uiState.value = SearchUiState.Error(error.localizedMessage ?: "Unknown search error occurred.")
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
