package find.my.train.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import find.my.train.domain.model.SearchHistory
import find.my.train.domain.model.Train
import find.my.train.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToLiveStatus: (String) -> Unit,
    onNavigateToBetweenStations: (String, String) -> Unit,
    onNavigateToPnr: () -> Unit,
    onNavigateToSchedule: (String) -> Unit,
    onNavigateToCoach: (String) -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val savedTrains by viewModel.savedTrainsState.collectAsState()
    val recentSearches by viewModel.recentSearchesState.collectAsState()

    val sourceQuery by viewModel.sourceQuery.collectAsState()
    val destQuery by viewModel.destQuery.collectAsState()
    val sourceCode by viewModel.sourceCode.collectAsState()
    val destCode by viewModel.destCode.collectAsState()

    val sourceSuggestions by viewModel.sourceSuggestions.collectAsState()
    val destSuggestions by viewModel.destSuggestions.collectAsState()

    var isSourceFocused by remember { mutableStateOf(false) }
    var isDestFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsTransit,
                            contentDescription = "Train Logo",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Find My Train",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            // Custom bottom bar with PNR and TICKETS tabs from the image
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shadowElevation = 8.dp,
                color = Color(0xFFE8F0FE) // Light blue tint
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // PNR tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onNavigateToPnr() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "PNR",
                                tint = Color(0xFF1967D2),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PNR",
                                color = Color(0xFF1967D2),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    // Vertical divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.LightGray)
                    )

                    // TICKETS tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onNavigateToSearch() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = "TICKETS",
                                tint = Color(0xFF1967D2),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TICKETS",
                                color = Color(0xFF1967D2),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF1F3F4)) // Neutral background
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Source/Destination Station Inputs Panel
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Left-side Route path connector (Circle -> Dotted Line -> Down Arrow -> Circle)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(110.dp)
                            ) {
                                // Source circle
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .border(2.dp, Color.Gray, CircleShape)
                                        .background(Color.White, CircleShape)
                                )

                                // Dotted line canvas with arrow in middle
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .width(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxHeight().width(2.dp)) {
                                        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                                        drawLine(
                                            color = Color.Gray,
                                            start = Offset(0f, 0f),
                                            end = Offset(0f, size.height),
                                            strokeWidth = 3f,
                                            pathEffect = pathEffect
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color.White)
                                    )
                                }

                                // Destination circle
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .border(2.dp, Color.Gray, CircleShape)
                                        .background(Color.White, CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Text Fields
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(110.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Source Input Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                                        .background(Color.White)
                                        .padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Code badge if selected
                                    if (sourceCode != null) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF1967D2), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = sourceCode!!,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }

                                    Box(modifier = Modifier.weight(1f)) {
                                        if (sourceQuery.isEmpty()) {
                                            Text(
                                                text = "From station",
                                                color = Color.Gray,
                                                fontSize = 16.sp
                                            )
                                        }
                                        BasicTextField(
                                            value = sourceQuery,
                                            onValueChange = { viewModel.onSourceQueryChange(it) },
                                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .onFocusChanged { isSourceFocused = it.isFocused }
                                        )
                                    }

                                    if (sourceQuery.isNotEmpty()) {
                                        IconButton(
                                            onClick = { viewModel.onSourceQueryChange("") },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                // Destination Input Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                                        .background(Color.White)
                                        .padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Code badge if selected
                                    if (destCode != null) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF1967D2), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = destCode!!,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }

                                    Box(modifier = Modifier.weight(1f)) {
                                        if (destQuery.isEmpty()) {
                                            Text(
                                                text = "To station",
                                                color = Color.Gray,
                                                fontSize = 16.sp
                                            )
                                        }
                                        BasicTextField(
                                            value = destQuery,
                                            onValueChange = { viewModel.onDestQueryChange(it) },
                                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .onFocusChanged { isDestFocused = it.isFocused }
                                        )
                                    }

                                    if (destQuery.isNotEmpty()) {
                                        IconButton(
                                            onClick = { viewModel.onDestQueryChange("") },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Swap Stations Button
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .clickable { viewModel.swapStations() }
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Swap",
                                    tint = Color(0xFF1A73E8),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        // Search Button
                        Button(
                            onClick = {
                                if (sourceQuery.isNotEmpty() && destQuery.isNotEmpty()) {
                                    val srcParam = sourceCode ?: sourceQuery
                                    val destParam = destCode ?: destQuery
                                    viewModel.saveSearch("$srcParam to $destParam", "STATIONS")
                                    onNavigateToBetweenStations(srcParam, destParam)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F9D58)), // Premium Green
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "FIND TRAINS",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // Recent Pinned Trains Slider (retained as helpful feature)
                if (savedTrains.isNotEmpty()) {
                    item {
                        Text(
                            text = "PINNED TRAINS",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 160.dp)
                        ) {
                            items(savedTrains) { train ->
                                PinnedTrainCard(
                                    train = train,
                                    onClick = { onNavigateToLiveStatus(train.number) },
                                    onScheduleClick = { onNavigateToSchedule(train.number) },
                                    onCoachClick = { onNavigateToCoach(train.number) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                // Search History Section
                item {
                    Text(
                        text = "SEARCH HISTORY",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                if (recentSearches.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No recent searches", color = Color.Gray)
                        }
                    }
                } else {
                    items(recentSearches) { search ->
                        // Render Search History matching screenshot design
                        val displayTitle: String
                        val routeInfo: String

                        if (search.query.contains("(")) {
                            displayTitle = search.query.substringBefore(" (").trim()
                            routeInfo = search.query.substringAfter("(").substringBefore(")").trim()
                        } else {
                            displayTitle = search.query
                            routeInfo = ""
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (search.type == "TRAIN") {
                                        val number = displayTitle.substringBefore(" ")
                                        onNavigateToLiveStatus(number)
                                    } else {
                                        val parts = search.query.split(" to ")
                                        val src = parts.getOrNull(0) ?: ""
                                        val dest = parts.getOrNull(1) ?: ""
                                        onNavigateToBetweenStations(src, dest)
                                    }
                                },
                            shape = RoundedCornerShape(0.dp), // flat items separated by divider
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = displayTitle,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (routeInfo.isNotEmpty()) {
                                        Text(
                                            text = routeInfo,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "View Details",
                                        tint = Color(0xFF0F9D58), // Green chevron/arrow matching screenshot
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            // Dropdown Popups for Source Search Suggestions
            if (isSourceFocused && sourceQuery.isNotEmpty() && sourceSuggestions.isNotEmpty()) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(focusable = false),
                    onDismissRequest = { isSourceFocused = false }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 135.dp) // align below From station
                            .heightIn(max = 240.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        LazyColumn {
                            items(sourceSuggestions) { suggestion ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectSourceStation(suggestion.code, suggestion.name)
                                            focusManager.clearFocus()
                                        }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Blue badge tag on left
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF1967D2), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = suggestion.code,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = suggestion.name,
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }

            // Dropdown Popups for Destination Search Suggestions
            if (isDestFocused && destQuery.isNotEmpty() && destSuggestions.isNotEmpty()) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(focusable = false),
                    onDismissRequest = { isDestFocused = false }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 190.dp) // align below To station
                            .heightIn(max = 240.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        LazyColumn {
                            items(destSuggestions) { suggestion ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectDestStation(suggestion.code, suggestion.name)
                                            focusManager.clearFocus()
                                        }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Blue badge tag on left
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF1967D2), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = suggestion.code,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = suggestion.name,
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PinnedTrainCard(
    train: Train,
    onClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onCoachClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = train.number,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = train.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = train.sourceCode,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.TrendingFlat,
                    contentDescription = "to",
                    tint = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = train.destinationCode,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = onScheduleClick,
                    modifier = Modifier.weight(1f).height(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Schedule", fontSize = 11.sp)
                }
                TextButton(
                    onClick = onCoachClick,
                    modifier = Modifier.weight(1f).height(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Coach", fontSize = 11.sp)
                }
            }
        }
    }
}

