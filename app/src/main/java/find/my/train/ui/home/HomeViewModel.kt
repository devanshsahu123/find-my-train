package find.my.train.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.data.mock.MockData
import find.my.train.domain.model.SearchHistory
import find.my.train.domain.model.Train
import find.my.train.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getSavedTrainsUseCase: GetSavedTrainsUseCase,
    getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val toggleSaveTrainUseCase: ToggleSaveTrainUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase
) : ViewModel() {

    val savedTrainsState: StateFlow<List<Train>> = getSavedTrainsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSearchesState: StateFlow<List<SearchHistory>> = getSearchHistoryUseCase(10)
        .map { list ->
            if (list.isEmpty()) {
                listOf(
                    SearchHistory(1, "12854 Amarkantak Express (RKMP - NDPM)", "TRAIN", System.currentTimeMillis() - 1000),
                    SearchHistory(2, "17020 Hisar Weekly Express (BPL - SDLP)", "TRAIN", System.currentTimeMillis() - 2000),
                    SearchHistory(3, "11464 Veraval Express (NDPM - RKMP)", "TRAIN", System.currentTimeMillis() - 3000),
                    SearchHistory(4, "20843 Bhagat Ki Kothi SF Express (NDPM - RKMP)", "TRAIN", System.currentTimeMillis() - 4000),
                    SearchHistory(5, "12615 Grand Trunk Express (NDPM - RKMP)", "TRAIN", System.currentTimeMillis() - 5000)
                )
            } else {
                list
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sourceQuery = MutableStateFlow("")
    val sourceQuery = _sourceQuery.asStateFlow()

    private val _destQuery = MutableStateFlow("")
    val destQuery = _destQuery.asStateFlow()

    private val _sourceCode = MutableStateFlow<String?>(null)
    val sourceCode = _sourceCode.asStateFlow()

    private val _destCode = MutableStateFlow<String?>(null)
    val destCode = _destCode.asStateFlow()

    val sourceSuggestions: StateFlow<List<find.my.train.data.remote.model.StationResponse>> = _sourceQuery
        .map { query ->
            if (query.length < 2) emptyList()
            else {
                MockData.stationsList.filter {
                    it.code.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val destSuggestions: StateFlow<List<find.my.train.data.remote.model.StationResponse>> = _destQuery
        .map { query ->
            if (query.length < 2) emptyList()
            else {
                MockData.stationsList.filter {
                    it.code.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSourceQueryChange(query: String) {
        _sourceQuery.value = query
        if (query.isEmpty()) {
            _sourceCode.value = null
        }
    }

    fun onDestQueryChange(query: String) {
        _destQuery.value = query
        if (query.isEmpty()) {
            _destCode.value = null
        }
    }

    fun selectSourceStation(code: String, name: String) {
        _sourceQuery.value = name
        _sourceCode.value = code
    }

    fun selectDestStation(code: String, name: String) {
        _destQuery.value = name
        _destCode.value = code
    }

    fun swapStations() {
        val tempQuery = _sourceQuery.value
        val tempCode = _sourceCode.value

        _sourceQuery.value = _destQuery.value
        _sourceCode.value = _destCode.value

        _destQuery.value = tempQuery
        _destCode.value = tempCode
    }

    fun toggleSaveTrain(train: Train) {
        viewModelScope.launch {
            toggleSaveTrainUseCase(train)
        }
    }

    fun saveSearch(query: String, type: String) {
        viewModelScope.launch {
            saveSearchHistoryUseCase(query, type)
        }
    }
}
