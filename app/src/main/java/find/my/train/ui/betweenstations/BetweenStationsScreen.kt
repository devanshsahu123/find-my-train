package find.my.train.ui.betweenstations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import find.my.train.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetweenStationsScreen(
    onBackClick: () -> Unit,
    onTrainClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BetweenStationsViewModel = hiltViewModel()
) {
    val source by viewModel.source.collectAsState()
    val destination by viewModel.destination.collectAsState()
    val date by viewModel.journeyDate.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val savedNumbers by viewModel.savedTrainNumbers.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trains Between Stations", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            OfflineBanner(visible = !isOnline)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search Route Summary Header
                item {
                    if (source.isNotEmpty() && destination.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "$source → $destination",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Date: $date",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                // Results heading
                item {
                    Text(
                        text = "Available Trains",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Result content listing
                when (val state = uiState) {
                    is BetweenStationsUiState.Empty -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Enter From and To stations above to look up train schedules.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                    is BetweenStationsUiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingView(message = "Fetching railway schedules...")
                            }
                        }
                    }
                    is BetweenStationsUiState.Error -> {
                        item {
                            ErrorView(
                                message = state.message,
                                onRetry = { viewModel.findTrains() }
                            )
                        }
                    }
                    is BetweenStationsUiState.Success -> {
                        if (state.trains.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No direct trains found between '$source' and '$destination'. Try using station codes like NDLS (New Delhi) or BSB (Varanasi).",
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        } else {
                            items(state.trains) { train ->
                                TrainCard(
                                    train = train,
                                    onClick = { onTrainClick(train.number) },
                                    isSaved = savedNumbers.contains(train.number),
                                    onSaveToggle = { viewModel.toggleSaveTrain(train) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
