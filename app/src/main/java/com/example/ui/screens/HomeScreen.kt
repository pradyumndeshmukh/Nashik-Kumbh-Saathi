package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.AppScreen
import com.example.ui.UserProfile
import com.example.ui.MainViewModel.CountdownState
import com.example.ui.components.AnimatedKumbhLogo
import com.example.ui.components.CountdownCard
import com.example.ui.components.CrowdAdvisoryBanner
import com.example.ui.components.TopSearchBar
import com.example.ui.components.WeatherWidget
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.MarigoldYellow
import com.example.ui.theme.SaffronLight
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr

@Composable
fun HomeScreen(
    countdownState: CountdownState,
    currentAdvisory: CrowdLevel,
    onAdvisoryChanged: (CrowdLevel) -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onNavigateToScreen: (AppScreen) -> Unit,
    onOpenCulture: () -> Unit,
    onToggleReminder: (String) -> Unit,
    reminderIds: Set<String>,
    userProfile: UserProfile? = null,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isSearching = searchQuery.trim().isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        // Sticky Search Bar at Top with Top-Left More Menu button
        TopSearchBar(
            query = searchQuery,
            onQueryChange = onQueryChanged,
            placeholderText = "search_placeholder".tr(appLanguage),
            onOpenDrawer = onOpenDrawer
        )

        if (isSearching) {
            SearchResultsContent(
                query = searchQuery,
                onNavigateToScreen = onNavigateToScreen,
                onClearSearch = { onQueryChanged("") }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Banner
                item {
                    HeroHeaderBanner(
                        onOpenCulture = onOpenCulture,
                        onOpenAuth = { onNavigateToScreen(if (userProfile?.isLoggedIn == true) AppScreen.PROFILE else AppScreen.AUTH) }
                    )
                }

                // Countdown Timer Card
                item {
                    PaddingWrapper {
                        CountdownCard(countdownState = countdownState, appLanguage = appLanguage)
                    }
                }

                // Today's Crowd Advisory Banner
                item {
                    PaddingWrapper {
                        CrowdAdvisoryBanner(
                            currentAdvisory = currentAdvisory,
                            onAdvisorySelected = onAdvisoryChanged,
                            onNavigateToMap = { onNavigateToScreen(AppScreen.MAP) },
                            appLanguage = appLanguage
                        )
                    }
                }

                // Weather Widget
                item {
                    PaddingWrapper {
                        WeatherWidget()
                    }
                }

                // Quick Access Grid Tiles
                item {
                    PaddingWrapper {
                        QuickAccessGrid(onNavigateToScreen = onNavigateToScreen, appLanguage = appLanguage)
                    }
                }

                // Lost Stuff & Missing Person Card on Home Screen
                item {
                    PaddingWrapper {
                        LostAndFoundSectionBanner(
                            onNavigateToEmergency = { onNavigateToScreen(AppScreen.EMERGENCY) }
                        )
                    }
                }

                // Upcoming Shahi Snan Events Preview
                item {
                    UpcomingEventsSection(
                        events = KumbhDataRepository.snanEvents.take(4),
                        reminderIds = reminderIds,
                        onToggleReminder = onToggleReminder,
                        onViewFullSchedule = { onNavigateToScreen(AppScreen.SCHEDULE) }
                    )
                }

                // Cultural & Guide Card
                item {
                    PaddingWrapper {
                        CultureGuideBanner(onOpenCulture = onOpenCulture)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaddingWrapper(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        content()
    }
}

@Composable
private fun HeroHeaderBanner(onOpenCulture: () -> Unit, onOpenAuth: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("hero_header_banner"),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.img_kumbh_hero),
                contentDescription = "Ramkund Nashik Kumbh Mela",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark Scrim Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MarigoldYellow,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "SIMHASTHA 2027",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Button(
                        onClick = onOpenAuth,
                        colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Account", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nashik - Trimbakeshwar Kumbh Saathi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Official Companion for Holy Pilgrimage & Crowd Navigation",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    AnimatedKumbhLogo(
                        size = 56.dp,
                        showHalo = true,
                        onClick = onOpenCulture
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickAccessGrid(
    onNavigateToScreen: (AppScreen) -> Unit,
    appLanguage: AppLanguage = AppLanguage.ENGLISH
) {
    Column {
        Text(
            text = "Quick Navigation".tr(appLanguage),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Snan Dates",
                subtitle = "Ritual Calendar",
                icon = Icons.Default.CalendarToday,
                color = Color(0xFFD84315),
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.SCHEDULE) }
            )
            QuickTile(
                title = "Map & Ghats",
                subtitle = "Live Locations",
                icon = Icons.Default.Map,
                color = Color(0xFF0288D1),
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.MAP) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Hotels & Stay",
                subtitle = "Tent City & Rooms",
                icon = Icons.Default.Hotel,
                color = Color(0xFF7B1FA2),
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.STAY) }
            )
            QuickTile(
                title = "Restaurants & Food",
                subtitle = "Misal & Veg Thali",
                icon = Icons.Default.Restaurant,
                color = Color(0xFF2E7D32),
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.FOOD) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Nearby Hospitals",
                subtitle = "24x7 Medicals",
                icon = Icons.Default.MedicalServices,
                color = Color(0xFFC62828),
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.MEDICALS) }
            )
            QuickTile(
                title = "Emergency",
                subtitle = "1-Tap Helpline",
                icon = Icons.Default.Emergency,
                color = Color(0xFF1565C0),
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.EMERGENCY) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Shuttle Buses",
                subtitle = "Citilinc & Routes",
                icon = Icons.Default.DirectionsBus,
                color = Color(0xFFE65100),
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.TRANSPORT) }
            )
            QuickTile(
                title = "Traditions",
                subtitle = "13 Akharas & Culture",
                icon = Icons.Default.AutoAwesome,
                color = SaffronPrimary,
                appLanguage = appLanguage,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToScreen(AppScreen.CULTURE) }
            )
        }
    }
}

@Composable
private fun QuickTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title.tr(appLanguage),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle.tr(appLanguage),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun UpcomingEventsSection(
    events: List<SnanEvent>,
    reminderIds: Set<String>,
    onToggleReminder: (String) -> Unit,
    onViewFullSchedule: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Key Shahi Snan Dates",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(onClick = onViewFullSchedule) {
                Text(
                    text = "View All →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                val isReminderSet = reminderIds.contains(event.id)
                EventMiniCard(
                    event = event,
                    isReminderSet = isReminderSet,
                    onToggleReminder = { onToggleReminder(event.id) }
                )
            }
        }
    }
}

@Composable
private fun EventMiniCard(
    event: SnanEvent,
    isReminderSet: Boolean,
    onToggleReminder: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(230.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (event.isMainShahiSnan) Color(0xFFFFE0B2) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (event.isMainShahiSnan) "SHAHI SNAN" else "RITUAL DATED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (event.isMainShahiSnan) Color(0xFFE65100) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onToggleReminder,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isReminderSet) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                        contentDescription = "Set Reminder",
                        tint = if (isReminderSet) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column {
                Text(
                    text = event.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = event.dateText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SaffronPrimary
                )
                Text(
                    text = event.ghatLocation,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun CultureGuideBanner(onOpenCulture: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenCulture() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SaffronPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Culture",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Kumbh Traditions & Etiquette Guide",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Learn about 13 Akharas, ghat rules & spiritual significance",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun LostAndFoundSectionBanner(
    onNavigateToEmergency: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onNavigateToEmergency() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1A0E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD32F2F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonSearch,
                            contentDescription = "Lost Person & Stuff",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Lost Stuff or Person Page",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Report missing relatives, children, phone or wallet",
                            fontSize = 11.sp,
                            color = Color(0xFFFFE0B2)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onNavigateToEmergency,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Report / Search", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3E2723), RoundedCornerShape(10.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Broadcasting",
                        tint = Color(0xFFFFB74D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Instant Police Loudspeaker & Mela Alerts",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFFFB74D),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchResultsContent(
    query: String,
    onNavigateToScreen: (AppScreen) -> Unit,
    onClearSearch: () -> Unit
) {
    val matchingEvents = KumbhDataRepository.snanEvents.filter {
        it.title.contains(query, ignoreCase = true) || it.ghatLocation.contains(query, ignoreCase = true)
    }
    val matchingPlaces = KumbhDataRepository.mapLocations.filter {
        it.name.contains(query, ignoreCase = true) || it.area.contains(query, ignoreCase = true)
    }
    val matchingStays = KumbhDataRepository.stayListings.filter {
        it.name.contains(query, ignoreCase = true) || it.area.contains(query, ignoreCase = true)
    }
    val matchingFood = KumbhDataRepository.foodListings.filter {
        it.name.contains(query, ignoreCase = true) || it.typeText.contains(query, ignoreCase = true)
    }

    val hasResults = matchingEvents.isNotEmpty() || matchingPlaces.isNotEmpty() || matchingStays.isNotEmpty() || matchingFood.isNotEmpty()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Search Results for \"$query\"",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextButton(onClick = onClearSearch) {
                    Text("Clear", color = SaffronPrimary, fontSize = 12.sp)
                }
            }
        }

        if (!hasResults) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "No Results",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No matching items found.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            if (matchingEvents.isNotEmpty()) {
                item {
                    Text("Snan Calendar Events", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                }
                items(matchingEvents) { event ->
                    SearchResultCard(title = event.title, subtitle = "${event.dateText} • ${event.ghatLocation}") {
                        onNavigateToScreen(AppScreen.SCHEDULE)
                    }
                }
            }

            if (matchingPlaces.isNotEmpty()) {
                item {
                    Text("Locations & Ghats", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                }
                items(matchingPlaces) { place ->
                    SearchResultCard(title = place.name, subtitle = "${place.area} • ${place.category.label}") {
                        onNavigateToScreen(AppScreen.MAP)
                    }
                }
            }

            if (matchingStays.isNotEmpty()) {
                item {
                    Text("Hotels & Stays", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                }
                items(matchingStays) { stay ->
                    SearchResultCard(title = stay.name, subtitle = "${stay.area} • ${stay.priceRange}") {
                        onNavigateToScreen(AppScreen.STAY)
                    }
                }
            }

            if (matchingFood.isNotEmpty()) {
                item {
                    Text("Food & Bhandara", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                }
                items(matchingFood) { food ->
                    SearchResultCard(title = food.name, subtitle = "${food.area} • ${food.typeText}") {
                        onNavigateToScreen(AppScreen.FOOD)
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SaffronPrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
