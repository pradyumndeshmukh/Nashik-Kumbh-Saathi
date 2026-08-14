package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.KumbhDataRepository
import com.example.data.MapCategory
import com.example.data.MapLocation
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MapScreen(
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(MapCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<MapLocation?>(KumbhDataRepository.mapLocations.first()) }
    var viewMode by remember { mutableStateOf("MAP") } // "MAP" or "LIST"

    val filteredLocations = KumbhDataRepository.mapLocations.filter { loc ->
        val matchesCategory = (selectedCategory == MapCategory.ALL || loc.category == selectedCategory)
        val matchesQuery = searchQuery.isEmpty() ||
                loc.name.contains(searchQuery, ignoreCase = true) ||
                loc.area.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("map_screen")
    ) {
        // Top Search & Category Filter Header
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
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
                                text = "Live Google Maps Directory".tr(appLanguage),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Interactive Google Maps with Ghats, Parking & Help Booths".tr(appLanguage),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // View Toggle (Map / Traffic / List)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = viewMode == "MAP",
                            onClick = { viewMode = "MAP" },
                            label = { Text("Map".tr(appLanguage), fontSize = 11.sp) },
                            leadingIcon = if (viewMode == "MAP") {
                                { Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaffronPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = viewMode == "TRAFFIC",
                            onClick = { viewMode = "TRAFFIC" },
                            label = { Text("Traffic".tr(appLanguage), fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Traffic,
                                    contentDescription = null,
                                    tint = if (viewMode == "TRAFFIC") Color.White else SaffronPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFC62828),
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = viewMode == "LIST",
                            onClick = { viewMode = "LIST" },
                            label = { Text("List".tr(appLanguage), fontSize = 11.sp) },
                            leadingIcon = if (viewMode == "LIST") {
                                { Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaffronPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search location, parking, ghat, medical...".tr(appLanguage), fontSize = 12.sp) },
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

                Spacer(modifier = Modifier.height(10.dp))

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(MapCategory.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.label.tr(appLanguage), fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaffronPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        if (viewMode == "MAP" || viewMode == "TRAFFIC") {
            // Live Integrated Google Maps / OSM Map Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE0E0E0))
            ) {
                val baseQuery = selectedLocation?.let { "${it.name}, ${it.area}, Nashik" } ?: "Ramkund Ghat Panchavati Nashik"
                val queryStr = if (viewMode == "TRAFFIC") "$baseQuery live traffic congestion" else baseQuery
                val directMapUrl = "https://maps.google.com/maps?q=${Uri.encode(queryStr)}&output=embed"

                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.databaseEnabled = true
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true
                            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                    return false
                                }
                            }
                            loadUrl(directMapUrl)
                        }
                    },
                    update = { webView ->
                        webView.loadUrl(directMapUrl)
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Traffic Legend Bar when Traffic Mode is active
                if (viewMode == "TRAFFIC") {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.85f),
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF388E3C)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Smooth".tr(appLanguage), fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF57C00)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Moderate".tr(appLanguage), fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFD32F2F)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Heavy".tr(appLanguage), fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF880E4F)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Diversion".tr(appLanguage), fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Quick Interactive Pins/Chips Bar on Top of Map Frame
                LazyRow(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredLocations) { loc ->
                        val isSelected = selectedLocation?.id == loc.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedLocation = loc },
                            label = { Text(loc.name.tr(appLanguage), fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = {
                                Icon(
                                    imageVector = getCategoryIcon(loc.category),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = if (isSelected) Color.White else SaffronPrimary
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepSaffron,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Direct Open Google Maps Floating Button
                SmallFloatingActionButton(
                    onClick = {
                        val mapsUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(queryStr)}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                    },
                    containerColor = DeepSaffron,
                    contentColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Google Maps".tr(appLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Bottom Selected Location Card
            selectedLocation?.let { loc ->
                LocationDetailCard(
                    location = loc,
                    appLanguage = appLanguage,
                    onGetDirections = {
                        val mapsUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode("${loc.name}, ${loc.area}, Nashik")}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                    },
                    onCallPhone = {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${loc.contactPhone}"))
                        context.startActivity(callIntent)
                    }
                )
            }
        } else {
            // List View
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredLocations) { loc ->
                    LocationListCard(
                        location = loc,
                        appLanguage = appLanguage,
                        onClick = {
                            selectedLocation = loc
                            viewMode = "MAP"
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LocationDetailCard(
    location: MapLocation,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onGetDirections: () -> Unit,
    onCallPhone: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = location.name.tr(appLanguage),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${location.area.tr(appLanguage)} • ${location.distanceKm} km from center",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getCategoryColor(location.category).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = location.category.label.tr(appLanguage),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = getCategoryColor(location.category),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = location.description.tr(appLanguage),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Facilities FlowRow
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                location.facilities.forEach { facility ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "• ${facility.tr(appLanguage)}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onGetDirections,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Get Directions".tr(appLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onCallPhone,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Helpdesk".tr(appLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun LocationListCard(
    location: MapLocation,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(getCategoryColor(location.category).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(location.category),
                    contentDescription = null,
                    tint = getCategoryColor(location.category),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = location.name.tr(appLanguage), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "${location.area.tr(appLanguage)} • ${location.distanceKm} km away", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = location.timing.tr(appLanguage), fontSize = 11.sp, color = SaffronPrimary)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = "View")
        }
    }
}

private fun getCategoryColor(category: MapCategory): Color {
    return when (category) {
        MapCategory.GHATS -> Color(0xFFD84315)
        MapCategory.TEMPLES -> Color(0xFFFF8F00)
        MapCategory.CAMPS -> Color(0xFF7B1FA2)
        MapCategory.PARKING -> Color(0xFF0288D1)
        MapCategory.MEDICAL -> Color(0xFFC62828)
        MapCategory.POLICE -> Color(0xFF283593)
        MapCategory.TOILETS -> Color(0xFF00695C)
        MapCategory.WATER -> Color(0xFF00838F)
        MapCategory.ALL -> SaffronPrimary
    }
}

private fun getCategoryIcon(category: MapCategory): ImageVector {
    return when (category) {
        MapCategory.GHATS -> Icons.Default.WaterDrop
        MapCategory.TEMPLES -> Icons.Default.AccountBalance
        MapCategory.CAMPS -> Icons.Default.Cabin
        MapCategory.PARKING -> Icons.Default.LocalParking
        MapCategory.MEDICAL -> Icons.Default.LocalHospital
        MapCategory.POLICE -> Icons.Default.LocalPolice
        MapCategory.TOILETS -> Icons.Default.Wc
        MapCategory.WATER -> Icons.Default.Water
        MapCategory.ALL -> Icons.Default.Place
    }
}
