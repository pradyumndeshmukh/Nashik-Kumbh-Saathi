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
fun FoodScreen(
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedHub by remember { mutableStateOf("All Locations") }
    var selectedRestaurantCategory by remember { mutableStateOf("ALL") }

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

    val restaurantCategories = listOf(
        "ALL" to "All Restaurants 🍽️",
        "Misal Pav" to "Famous Misal Pav 🌶️",
        "Pure Veg Thali" to "Pure Veg Thali 🥗",
        "Family Restaurant" to "Family Dining 🍛",
        "Snacks & Upvas" to "Snacks & Fasting ☕"
    )

    val filteredFood = KumbhDataRepository.foodListings.filter { food ->
        val matchesHub = selectedHub == "All Locations" || food.locationHub == selectedHub
        val matchesCategory = (selectedRestaurantCategory == "ALL" || food.category.equals(selectedRestaurantCategory, ignoreCase = true))
        val matchesQuery = searchQuery.isEmpty() ||
                food.name.contains(searchQuery, ignoreCase = true) ||
                food.area.contains(searchQuery, ignoreCase = true) ||
                food.typeText.contains(searchQuery, ignoreCase = true) ||
                food.description.contains(searchQuery, ignoreCase = true)
        matchesHub && matchesCategory && matchesQuery
    }.sortedByDescending { it.rating }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("food_screen")
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
                            text = "Restaurants & Local Food Directory".tr(appLanguage),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Famous Nashik Misal Pav, Pure Veg Thalis, Upvas Fasting Food & Google Maps".tr(appLanguage),
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
                    placeholder = { Text("Search Misal Pav, Thali, restaurant...".tr(appLanguage), fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DeepSaffron, modifier = Modifier.size(18.dp)) },
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
                        focusedBorderColor = DeepSaffron
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
            contentColor = DeepSaffron,
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
                            color = if (isSelected) DeepSaffron else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // Category Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(restaurantCategories) { (catKey, catLabel) ->
                val isSelected = selectedRestaurantCategory == catKey
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedRestaurantCategory = catKey },
                    label = { Text(catLabel.tr(appLanguage), fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeepSaffron,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // List Content with Google Maps Integration Header
        if (filteredFood.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No restaurants found matching your filters.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = {
                        selectedHub = "All Locations"
                        selectedRestaurantCategory = "ALL"
                        searchQuery = ""
                    }) {
                        Text("Reset Filters", color = DeepSaffron)
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
                    // Google Maps Hero Search Card with Nearby Restaurants Integration
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1A0E)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Restaurants in ${if (selectedHub == "All Locations") "All Key Zones" else selectedHub}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Showing ${filteredFood.size} famous food spots with Google Maps navigation",
                                        fontSize = 11.sp,
                                        color = Color(0xFFFFE0B2)
                                    )
                                }

                                Button(
                                    onClick = {
                                        val targetArea = if (selectedHub == "All Locations") "Nashik Kumbh Mela" else selectedHub
                                        val queryStr = "Top restaurants and misal pav near $targetArea"
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

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "ONE-TAP MAP SEARCHES:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFFB74D)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val targetArea = if (selectedHub == "All Locations") "Nashik" else selectedHub
                                val mapQuickSearches = listOf(
                                    "Misal Pav Near Me" to "Best Misal Pav near $targetArea",
                                    "Pure Veg Thali" to "Pure Veg Thali restaurant near $targetArea",
                                    "Family Dining" to "Family veg restaurant near $targetArea",
                                    "Tea & Fasting Food" to "Tea and Upvas snacks near $targetArea"
                                )
                                items(mapQuickSearches) { (tag, query) ->
                                    Button(
                                        onClick = {
                                            val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(query)}")
                                            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                        },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        modifier = Modifier.height(32.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A2E1B))
                                    ) {
                                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFFFB74D), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(tag, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                items(filteredFood) { food ->
                    RestaurantCardItem(
                        food = food,
                        onCallPhone = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${food.phone}"))
                            context.startActivity(intent)
                        },
                        onOpenGoogleMaps = {
                            val queryStr = food.mapQuery.ifBlank { "${food.name}, ${food.address}" }
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

@Composable
private fun RestaurantCardItem(food: FoodListing, onCallPhone: () -> Unit, onOpenGoogleMaps: () -> Unit) {
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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = food.name, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "${food.typeText} • ${food.area}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        Text(text = "${food.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DeepSaffron.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = food.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepSaffron,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = food.priceForTwo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "• ${food.timing}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = food.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

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
                    Text("Call Restaurant", fontSize = 12.sp)
                }
            }
        }
    }
}
