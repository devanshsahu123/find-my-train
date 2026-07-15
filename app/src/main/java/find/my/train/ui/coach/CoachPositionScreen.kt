package find.my.train.ui.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import find.my.train.domain.model.CoachLayout
import find.my.train.domain.model.CoachMetadata
import find.my.train.domain.model.SeatInfo
import find.my.train.ui.components.ErrorView
import find.my.train.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachPositionScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoachPositionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Coach Position", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Train #${viewModel.trainNumber}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
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
            when (val state = uiState) {
                is CoachUiState.Loading -> {
                    LoadingView(message = "Loading train compositions...")
                }
                is CoachUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.fetchCoachLayout() }
                    )
                }
                is CoachUiState.Success -> {
                    CoachLayoutContent(coachLayout = state.coachLayout)
                }
            }
        }
    }
}

@Composable
fun CoachLayoutContent(coachLayout: CoachLayout) {
    var selectedCoach by remember {
        mutableStateOf(
            coachLayout.coaches.firstOrNull { it.label != "LOCO" && it.label != "EOG" }
                ?: coachLayout.coaches.firstOrNull()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Train header name card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsTransit,
                    contentDescription = "Train composition",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = coachLayout.trainName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Scroll and tap a coach to inspect its seat layout.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Horizontal Train Layout composition slider
        Text(
            text = "Train Carriage Sequence",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(coachLayout.coaches) { coach ->
                CoachCarriageItem(
                    coach = coach,
                    isSelected = selectedCoach == coach,
                    onClick = { selectedCoach = coach }
                )
            }
        }

        // Interactive Seat Map of the selected coach
        selectedCoach?.let { coach ->
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Coach ${coach.label} Layout (${coach.type})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Dynamic Seat layout generator
            val seats = remember(coach) { generateMockSeatsForCoach(coach.type) }
            
            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = MaterialTheme.colorScheme.primaryContainer, text = "Available")
                LegendItem(color = MaterialTheme.colorScheme.surfaceVariant, text = "Booked")
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Grid of seats
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (coach.type.contains("Chair")) 5 else 4),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(seats.size) { index ->
                    val seat = seats[index]
                    if (seat.seatNo == -1) {
                        // Represents the Aisle walk-through area
                        Box(modifier = Modifier.size(44.dp))
                    } else {
                        SeatLayoutItem(seat = seat)
                    }
                }
            }
        }
    }
}

@Composable
fun CoachCarriageItem(
    coach: CoachMetadata,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isLocoOrEog = coach.label == "LOCO" || coach.label == "EOG"
    val containerColor = when {
        isSelected -> MaterialTheme.colorScheme.secondary
        isLocoOrEog -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        else -> MaterialTheme.colorScheme.primary
    }
    val contentColor = when {
        isSelected -> MaterialTheme.colorScheme.onSecondary
        isLocoOrEog -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onPrimary
    }

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = coach.label,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = contentColor
            )
            Text(
                text = if (coach.label == "LOCO") "Engine" else if (coach.label == "EOG") "Power" else "Pax",
                fontSize = 9.sp,
                color = contentColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun SeatLayoutItem(seat: SeatInfo) {
    val cardColor = if (seat.isBooked) MaterialTheme.colorScheme.surfaceVariant
    else MaterialTheme.colorScheme.primaryContainer
    
    val contentColor = if (seat.isBooked) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    else MaterialTheme.colorScheme.onPrimaryContainer

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(cardColor)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = seat.seatNo.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = contentColor
            )
            Text(
                text = seat.berthType,
                fontSize = 9.sp,
                color = contentColor.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

// Generate realistic coach layouts
private fun generateMockSeatsForCoach(type: String): List<SeatInfo> {
    val seatsList = mutableListOf<SeatInfo>()
    
    if (type.contains("Chair")) {
        // Vande Bharat / Shatabdi Chair Car layout (3 + Aisle + 2 layout)
        var seatCounter = 1
        for (row in 1..8) {
            // A, B, C seats
            seatsList.add(SeatInfo(seatCounter++, "WS", row % 3 == 0))
            seatsList.add(SeatInfo(seatCounter++, "MB", row % 2 == 0))
            seatsList.add(SeatInfo(seatCounter++, "AS", row % 5 == 0))
            
            // Aisle spacer (-1)
            seatsList.add(SeatInfo(-1, "Aisle", false))
            
            // D, E seats
            seatsList.add(SeatInfo(seatCounter++, "AS", row % 4 == 0))
            seatsList.add(SeatInfo(seatCounter++, "WS", row % 2 == 0))
        }
    } else {
        // Sleeper / 3AC Coach Layout (Bay of 6 berths + Aisle + 2 side berths)
        var seatCounter = 1
        for (bay in 1..6) {
            // Main Cabin berths
            seatsList.add(SeatInfo(seatCounter++, "LB", bay % 3 == 0))
            seatsList.add(SeatInfo(seatCounter++, "MB", bay % 2 == 0))
            seatsList.add(SeatInfo(seatCounter++, "UB", bay % 4 == 0))
            
            // Aisle spacer
            seatsList.add(SeatInfo(-1, "Aisle", false))
            
            // Side berths
            seatsList.add(SeatInfo(seatCounter++, "SL", bay % 5 == 0))
            seatsList.add(SeatInfo(seatCounter++, "SU", bay % 2 == 0))
            
            // Rest of Cabin (we need to align berths to rows)
            seatsList.add(SeatInfo(seatCounter++, "LB", bay % 2 == 0))
            seatsList.add(SeatInfo(seatCounter++, "MB", bay % 3 == 0))
            seatsList.add(SeatInfo(seatCounter++, "UB", bay % 4 == 0))
            
            // Spacer to keep layout structured
            seatsList.add(SeatInfo(-1, "Aisle", false))
        }
    }
    
    return seatsList
}
