package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StayScreen(
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedHub by remember { mutableStateOf("All Locations") }
    var selectedStayCategory by remember { mutableStateOf(StayCategory.ALL) }

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

    val filteredStays = KumbhDataRepository.stayListings.filter { stay ->
        val matchesHub = selectedHub == "All Locations" || stay.locationHub == selectedHub
        val matchesCategory = (selectedStayCategory == StayCategory.ALL || stay.category == selectedStayCategory)
        val matchesQuery = searchQuery.isEmpty() ||
                stay.name.contains(searchQuery, ignoreCase = true) ||
                stay.area.contains(searchQuery, ignoreCase = true) ||
                stay.address.contains(searchQuery, ignoreCase = true)
        matchesHub && matchesCategory && matchesQuery
    }.sortedByDescending { it.rating }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("stay_screen")
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
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
                            text = "Hotels & Dharamshalas Directory".tr(appLanguage),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Find Tent Cities, Dharamshalas & Hotels across all locations with Google Maps".tr(appLanguage),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Input with visible typed text
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search hotel, Dharamshala, tent city...".tr(appLanguage), fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = SaffronPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    singleLine = true
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
                            text = hub.tr(appLanguage),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // Stay Category Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(StayCategory.values()) { category ->
                FilterChip(
                    selected = selectedStayCategory == category,
                    onClick = { selectedStayCategory = category },
                    label = { Text(category.label.tr(appLanguage), fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // List Content
        if (filteredStays.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Hotel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No stays found matching your filters.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = {
                        selectedHub = "All Locations"
                        selectedStayCategory = StayCategory.ALL
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
                    // Google Maps Quick Search Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1A0E)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Hotels in ${if (selectedHub == "All Locations") "All Key Zones" else selectedHub}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Showing ${filteredStays.size} verified stays with live Google Maps navigation",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFFE0B2)
                                )
                            }

                            Button(
                                onClick = {
                                    val queryStr = "Hotels near ${if (selectedHub == "All Locations") "Nashik Kumbh Mela" else selectedHub}"
                                    val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(queryStr)}"))
                                    context.startActivity(mapsIntent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron)
                            ) {
                                Icon(Icons.Default.Map, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Map View", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                items(filteredStays) { stay ->
                    StayCardItem(
                        stay = stay,
                        onCallPhone = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${stay.phone}"))
                            context.startActivity(intent)
                        },
                        onOpenGoogleMaps = {
                            val queryStr = stay.mapQuery.ifBlank { "${stay.name}, ${stay.address}" }
                            val mapsUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(queryStr)}")
                            try {
                                context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                            } catch (e: Exception) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(queryStr)}")))
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StayCardItem(stay: StayListing, onCallPhone: () -> Unit, onOpenGoogleMaps: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = stay.name, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "${stay.area} • ${stay.distanceToGhat}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF8E1)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${stay.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = stay.priceRange, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = SaffronPrimary)

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                stay.amenities.forEach { amenity ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = amenity,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenGoogleMaps,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepSaffron)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Google Maps", fontSize = 12.sp)
                }

                Button(
                    onClick = onCallPhone,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Contact Stay", fontSize = 12.sp)
                }
            }
        }
    }
}
