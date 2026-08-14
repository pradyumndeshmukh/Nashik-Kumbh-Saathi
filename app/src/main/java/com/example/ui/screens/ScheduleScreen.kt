package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CrowdLevel
import com.example.data.KumbhDataRepository
import com.example.data.SnanEvent
import com.example.ui.theme.MarigoldYellow
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr

@Composable
fun ScheduleScreen(
    reminderIds: Set<String>,
    onToggleReminder: (String) -> Unit,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showMainShahiOnly by remember { mutableStateOf(false) }
    var filterQuery by remember { mutableStateOf("") }
    var expandedEventId by remember { mutableStateOf<String?>(null) }

    val filteredEvents = KumbhDataRepository.snanEvents.filter { event ->
        val matchesMain = if (showMainShahiOnly) event.isMainShahiSnan else true
        val matchesQuery = filterQuery.isEmpty() ||
                event.title.contains(filterQuery, ignoreCase = true) ||
                event.ritualName.contains(filterQuery, ignoreCase = true) ||
                event.ghatLocation.contains(filterQuery, ignoreCase = true)
        matchesMain && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("schedule_screen")
    ) {
        // Header Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Event Schedule & Snan Calendar".tr(appLanguage),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Simhastha Kumbh Mela 2027 • Nashik & Trimbakeshwar".tr(appLanguage),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Box
                OutlinedTextField(
                    value = filterQuery,
                    onValueChange = { filterQuery = it },
                    placeholder = { Text("Filter rituals, dates, or ghats...".tr(appLanguage), fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SaffronPrimary) },
                    trailingIcon = {
                        if (filterQuery.isNotEmpty()) {
                            IconButton(onClick = { filterQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Switch Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = !showMainShahiOnly,
                        onClick = { showMainShahiOnly = false },
                        label = { Text("All Events (${KumbhDataRepository.snanEvents.size})".tr(appLanguage)) },
                        leadingIcon = if (!showMainShahiOnly) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronPrimary,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = showMainShahiOnly,
                        onClick = { showMainShahiOnly = true },
                        label = { Text("Main Shahi Snans Only".tr(appLanguage)) },
                        leadingIcon = if (showMainShahiOnly) {
                            { Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFE65100),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Timeline List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredEvents) { event ->
                val isReminderSet = reminderIds.contains(event.id)
                val isExpanded = expandedEventId == event.id

                SnanEventCard(
                    event = event,
                    isReminderSet = isReminderSet,
                    isExpanded = isExpanded,
                    appLanguage = appLanguage,
                    onToggleReminder = { onToggleReminder(event.id) },
                    onToggleExpand = {
                        expandedEventId = if (isExpanded) null else event.id
                    }
                )
            }
        }
    }
}

@Composable
private fun SnanEventCard(
    event: SnanEvent,
    isReminderSet: Boolean,
    isExpanded: Boolean,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onToggleReminder: () -> Unit,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("snan_event_card_${event.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isMainShahiSnan) 
                MaterialTheme.colorScheme.surface 
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (event.isMainShahiSnan) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFF3E0),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Shahi Snan",
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SHAHI / AMRIT SNAN".tr(appLanguage),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "PARVA SNAN".tr(appLanguage),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Crowd Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(event.crowdLevel.hexColor).copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = event.crowdLevel.label.tr(appLanguage),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(event.crowdLevel.hexColor)
                        )
                    }
                }

                // Reminder Button
                IconButton(
                    onClick = onToggleReminder,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isReminderSet) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                        contentDescription = "Set Reminder",
                        tint = if (isReminderSet) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = event.title.tr(appLanguage),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${event.dateText.tr(appLanguage)} • ${event.timings.tr(appLanguage)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = event.ghatLocation.tr(appLanguage),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.significance.tr(appLanguage),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Expand Toggle Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Hide Bathing Guidelines".tr(appLanguage) else "View Guidelines & Crowd Rules".tr(appLanguage),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = SaffronPrimary
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Pilgrim Guidelines:".tr(appLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = event.guidelines.tr(appLanguage),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onToggleReminder,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isReminderSet) Icons.Default.Check else Icons.Default.AddAlert,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isReminderSet) "Reminder Saved to Calendar".tr(appLanguage) else "Add Event to Calendar".tr(appLanguage))
                    }
                }
            }
        }
    }
}
