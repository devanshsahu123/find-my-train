package find.my.train.ui.pnr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.domain.model.PnrStatus
import find.my.train.domain.usecase.GetPnrStatusUseCase
import find.my.train.util.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PnrUiState {
    object Empty : PnrUiState
    object Loading : PnrUiState
    data class Success(val pnrStatus: PnrStatus) : PnrUiState
    data class Error(val message: String) : PnrUiState
}

@HiltViewModel
class PnrViewModel @Inject constructor(
    private val getPnrStatusUseCase: GetPnrStatusUseCase,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _pnrQuery = MutableStateFlow("")
    val pnrQuery = _pnrQuery.asStateFlow()

    private val _uiState = MutableStateFlow<PnrUiState>(PnrUiState.Empty)
    val uiState = _uiState.asStateFlow()

    fun onPnrQueryChange(query: String) {
        _pnrQuery.value = query
    }

    fun checkPnrStatus() {
        val pnr = _pnrQuery.value.trim()
        if (pnr.length != 10 || !pnr.all { it.isDigit() }) {
            _uiState.value = PnrUiState.Error("PNR number must be exactly 10 digits.")
            return
        }

        viewModelScope.launch {
            _uiState.value = PnrUiState.Loading
            getPnrStatusUseCase(pnr, isOnline.value).fold(
                onSuccess = { pnrStatus ->
                    _uiState.value = PnrUiState.Success(pnrStatus)
                },
                onFailure = { error ->
                    _uiState.value = PnrUiState.Error(error.localizedMessage ?: "Failed to fetch PNR status.")
                }
            )
        }
    }
}
