package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KumbhDataRepository
import com.example.data.MedicalFacility
import com.example.data.MedicalType
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr

@Composable
fun MedicalScreen(
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    onShowToast: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val locationHubs = listOf(
        "All Locations",
        "Ramkund & Panchavati",
        "Trimbakeshwar",
        "Nashik Railway Station",
        "CBS Bus Stand",
        "Nimani Bus Stand",
        "Tapovan & Sadhugram",
        "Satpur & Ambad"
    )

    var selectedHub by remember { mutableStateOf("All Locations") }
    var selectedType by remember { mutableStateOf(MedicalType.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredFacilities = remember(selectedHub, selectedType, searchQuery) {
        KumbhDataRepository.medicalFacilities.filter { facility ->
            val matchesHub = selectedHub == "All Locations" || facility.locationHub == selectedHub
            val matchesType = selectedType == MedicalType.ALL || facility.facilityType == selectedType
            val matchesQuery = searchQuery.isBlank() ||
                    facility.name.contains(searchQuery, ignoreCase = true) ||
                    facility.address.contains(searchQuery, ignoreCase = true) ||
                    facility.services.any { it.contains(searchQuery, ignoreCase = true) }
            matchesHub && matchesType && matchesQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("medical_screen")
    ) {
        // Header Surface
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
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
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFC62828)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Medicals & Nearby Hospitals".tr(appLanguage),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "24x7 Emergency Hospitals & Pharmacies Across All Locations".tr(appLanguage),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search hospital, pharmacy, medicine or location...".tr(appLanguage), fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SaffronPrimary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SaffronPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )
            }
        }

        // Location Hub Filter Tabs (Scrollable)
        ScrollableTabRow(
            selectedTabIndex = locationHubs.indexOf(selectedHub).coerceAtLeast(0),
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            contentColor = SaffronPrimary,
            divider = {}
        ) {
            locationHubs.forEach { hub ->
                val isSelected = selectedHub == hub
                Tab(
                    selected = isSelected,
                    onClick = { selectedHub = hub },
                    text = {
                        Text(
                            text = hub,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // Type Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(MedicalType.values()) { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type.label, fontSize = 11.sp) },
                    leadingIcon = {
                        val icon = when (type) {
                            MedicalType.ALL -> Icons.Default.LocalHospital
                            MedicalType.HOSPITAL -> Icons.Default.LocalHospital
                            MedicalType.MEDICAL_STORE -> Icons.Default.Medication
                            MedicalType.GOVT_CAMP -> Icons.Default.MedicalInformation
                        }
                        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    )
                )
            }
        }

        // List of Medical Facilities
        if (filteredFacilities.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No medical facilities found for selected filter.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = {
                        selectedHub = "All Locations"
                        selectedType = MedicalType.ALL
                        searchQuery = ""
                    }) {
                        Text("Reset Filters", color = SaffronPrimary)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "Showing ${filteredFacilities.size} Facilities (${if (selectedHub == "All Locations") "All Key Zones" else selectedHub})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(filteredFacilities) { facility ->
                    MedicalFacilityCard(
                        facility = facility,
                        onCall = { phone ->
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            context.startActivity(intent)
                        },
                        onOpenMap = { query ->
                            val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(query.ifBlank { facility.address }))
                            val intent = Intent(Intent.ACTION_VIEW, mapUri)
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Fallback to browser web maps
                                val webMapUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query.ifBlank { facility.address }))
                                context.startActivity(Intent(Intent.ACTION_VIEW, webMapUri))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MedicalFacilityCard(
    facility: MedicalFacility,
    onCall: (String) -> Unit,
    onOpenMap: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val iconVector = when (facility.facilityType) {
                        MedicalType.HOSPITAL -> Icons.Default.LocalHospital
                        MedicalType.MEDICAL_STORE -> Icons.Default.Medication
                        MedicalType.GOVT_CAMP, MedicalType.ALL -> Icons.Default.HealthAndSafety
                    }

                    val badgeColor = when (facility.facilityType) {
                        MedicalType.HOSPITAL -> Color(0xFFC62828)
                        MedicalType.MEDICAL_STORE -> Color(0xFF2E7D32)
                        MedicalType.GOVT_CAMP, MedicalType.ALL -> Color(0xFF0288D1)
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(badgeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = facility.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = facility.locationHub,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepSaffron
                        )
                    }
                }

                if (facility.is24x7) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = "24x7 OPEN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Address & Distance
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${facility.address} (${facility.distance})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (facility.emergencyBeds.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Hotel,
                        contentDescription = null,
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = facility.emergencyBeds,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Available Services Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(facility.services) { service ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = "• $service",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onCall(facility.phone) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Helpline", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { onOpenMap(facility.mapQuery) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronPrimary)
                ) {
                    Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Get Directions", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
