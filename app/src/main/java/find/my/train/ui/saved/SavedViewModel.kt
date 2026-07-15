package find.my.train.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import find.my.train.domain.model.Train
import find.my.train.domain.usecase.GetSavedTrainsUseCase
import find.my.train.domain.usecase.ToggleSaveTrainUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    getSavedTrainsUseCase: GetSavedTrainsUseCase,
    private val toggleSaveTrainUseCase: ToggleSaveTrainUseCase
) : ViewModel() {

    val savedTrainsState: StateFlow<List<Train>> = getSavedTrainsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleSave(train: Train) {
        viewModelScope.launch {
            toggleSaveTrainUseCase(train)
        }
    }
}
