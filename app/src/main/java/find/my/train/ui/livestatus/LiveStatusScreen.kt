package find.my.train.ui.livestatus

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import find.my.train.domain.model.LiveStatus
import find.my.train.domain.model.Station
import find.my.train.domain.model.Train
import find.my.train.ui.components.ErrorView
import find.my.train.ui.components.LoadingView
import find.my.train.ui.components.OfflineBanner
import find.my.train.ui.theme.StatusDelayed
import find.my.train.ui.theme.StatusOnTime
import find.my.train.ui.theme.StatusSlightDelay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStatusScreen(
    onBackClick: () -> Unit,
    onScheduleClick: (String) -> Unit,
    onCoachClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LiveStatusViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Live Status", fontWeight = FontWeight.Bold)
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
                actions = {
                    if (uiState is LiveStatusUiState.Success) {
                        val status = (uiState as LiveStatusUiState.Success).status
                        IconButton(
                            onClick = {
                                viewModel.toggleSave(
                                    Train(
                                        number = status.trainNumber,
                                        name = status.trainName,
                                        source = status.routeStations.firstOrNull()?.name ?: "Source",
                                        destination = status.routeStations.lastOrNull()?.name ?: "Destination",
                                        sourceCode = status.routeStations.firstOrNull()?.code ?: "SRC",
                                        destinationCode = status.routeStations.lastOrNull()?.code ?: "DEST",
                                        departureTime = status.routeStations.firstOrNull()?.departureTime ?: "00:00",
                                        arrivalTime = status.routeStations.lastOrNull()?.arrivalTime ?: "00:00",
                                        runningDays = listOf("Daily"),
                                        trainType = "Express",
                                        travelTime = "8h"
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Pin train",
                                tint = if (isSaved) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onPrimary
                            )
                        }
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

            when (val state = uiState) {
                is LiveStatusUiState.Loading -> {
                    LoadingView(message = "Locating train coordinates...")
                }
                is LiveStatusUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { /* Will trigger reload through isOnline */ }
                    )
                }
                is LiveStatusUiState.Success -> {
                    LiveStatusContent(
                        status = state.status,
                        onScheduleClick = { onScheduleClick(state.status.trainNumber) },
                        onCoachClick = { onCoachClick(state.status.trainNumber) }
                    )
                }
            }
        }
    }
}

@Composable
fun LiveStatusContent(
    status: LiveStatus,
    onScheduleClick: () -> Unit,
    onCoachClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Panel (Train summary and message)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = status.trainName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when {
                                        status.delayMinutes == 0 -> StatusOnTime.copy(alpha = 0.1f)
                                        status.delayMinutes < 15 -> StatusSlightDelay.copy(alpha = 0.1f)
                                        else -> StatusDelayed.copy(alpha = 0.1f)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = when {
                                    status.delayMinutes == 0 -> "On Time"
                                    status.delayMinutes < 15 -> "${status.delayMinutes}m Late"
                                    else -> "${status.delayMinutes}m Late"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    status.delayMinutes == 0 -> StatusOnTime
                                    status.delayMinutes < 15 -> StatusSlightDelay
                                    else -> StatusDelayed
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = status.statusMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    // Metrics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricItem(
                            icon = Icons.Default.Speed,
                            label = "Speed",
                            value = "${status.speedKmh} km/h"
                        )
                        MetricItem(
                            icon = Icons.Default.Place,
                            label = "Platform",
                            value = "PF ${status.platform}"
                        )
                        MetricItem(
                            icon = Icons.Default.AccessTime,
                            label = "Updated",
                            value = status.lastUpdated
                        )
                    }
                }
            }
        }

        // Navigation Actions Card
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onScheduleClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Schedule, contentDescription = "Schedule")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Route", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onCoachClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.DirectionsTransit, contentDescription = "Coach Layout")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Coach Layout", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Timeline Header
        item {
            Text(
                text = "Route timeline",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Timeline Items
        itemsIndexed(status.routeStations) { index, station ->
            val isTrainNearThisStation = status.currentStationCode == station.code
            StationTimelineItem(
                station = station,
                isPassed = station.isPassed,
                isCurrent = isTrainNearThisStation,
                isLast = index == status.routeStations.size - 1,
                routeProgress = status.routeProgress,
                index = index
            )
        }
    }
}

@Composable
fun MetricItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun StationTimelineItem(
    station: Station,
    isPassed: Boolean,
    isCurrent: Boolean,
    isLast: Boolean,
    routeProgress: Float,
    index: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Vertical Timeline Track column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(48.dp)
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCurrent -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                            isPassed -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isCurrent) 14.dp else 10.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> MaterialTheme.colorScheme.secondary
                                isPassed -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            }
                        )
                )
            }

            if (!isLast) {
                // Draw vertical line connecting stops
                Spacer(modifier = Modifier.height(4.dp))
                val lineColor = if (isPassed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .fillMaxHeight()
                        .background(
                            color = lineColor,
                            shape = RoundedCornerShape(1.5.dp)
                        )
                )
            }
        }

        // Station Details Column
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrent) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = if (isCurrent) 1.dp else 0.dp,
                color = if (isCurrent) MaterialTheme.colorScheme.secondary else Color.Transparent
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${station.name} (${station.code})",
                        fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isCurrent) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (station.distanceKm > 0) "${station.distanceKm} km • PF ${station.platform}" else "PF ${station.platform}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                // Station scheduled and estimated arrival times
                Column(horizontalAlignment = Alignment.End) {
                    val actualArrival = station.actualArrivalTime ?: station.arrivalTime ?: station.actualDepartureTime ?: station.departureTime ?: "--:--"
                    val schedArrival = station.arrivalTime ?: station.departureTime ?: "--:--"
                    
                    Text(
                        text = actualArrival,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = if (isPassed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (schedArrival != actualArrival && schedArrival != "--:--") {
                        Text(
                            text = "Sched: $schedArrival",
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}
