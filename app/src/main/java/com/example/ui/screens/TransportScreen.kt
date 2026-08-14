package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.KumbhDataRepository
import com.example.data.TransportRoute
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr

@Composable
fun TransportScreen(
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val originOptions = listOf(
        "Nashik Road Railway Station",
        "Ojhar Nashik Airport (ISK)",
        "Tapovan Mega Parking Hub",
        "CBS Central Bus Stand",
        "Panchavati Ring Road"
    )

    val destinationOptions = listOf(
        "Ramkund Ghat (Panchavati)",
        "Kushavarta Kund (Trimbakeshwar)",
        "Sadhugram Tent City (Tapovan)",
        "Sri Kalaram Temple"
    )

    var selectedFrom by remember { mutableStateOf(originOptions.first()) }
    var selectedTo by remember { mutableStateOf(destinationOptions.first()) }
    var isFromExpanded by remember { mutableStateOf(false) }
    var isToExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("transport_screen")
    ) {
        // Header
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
                    Column {
                        Text(
                            text = "Transport & Travel Guide".tr(appLanguage),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Citilinc City Buses, Uber/Ola Rides, Parking Hubs & Shuttles".tr(appLanguage),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nashik Citilinc & Uber Integration Hero Cards
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Nashik Citilinc City Bus Service",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Official Nashik Municipal Corporation Bus Portal",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "• 250+ Electric Buses running every 3-5 mins on main Kumbh routes\n• Free E-Bus Shuttles between Parking Hubs & Ghat Gates\n• Live Vehicle Tracking & Route Timetable available online",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val citilincUri = Uri.parse("https://citilinc.nashikcorporation.in/")
                                    context.startActivity(Intent(Intent.ACTION_VIEW, citilincUri))
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                            ) {
                                Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Citilinc Website", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18002330011"))
                                    context.startActivity(callIntent)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Bus Helpline", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Uber & Ola Cab Booking Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalTaxi, contentDescription = null, tint = DeepSaffron, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Book Uber & Ola Cab / Auto",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Point-to-Point rides to outer drop-off zones",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val uberUri = Uri.parse("https://m.uber.com/ul/?action=setPickup&pickup=my_location&dropoff[formatted_address]=Ramkund+Ghat+Nashik")
                                    context.startActivity(Intent(Intent.ACTION_VIEW, uberUri))
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron)
                            ) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Book Uber", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val olaUri = Uri.parse("https://book.olacabs.com/")
                                    context.startActivity(Intent(Intent.ACTION_VIEW, olaUri))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ElectricRickshaw, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Book Ola", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // "Plan My Route" Interactive Form Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Directions, contentDescription = null, tint = SaffronPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Plan My Route",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // From Dropdown
                        Text("FROM (Transit Hub / Entry):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Box {
                            OutlinedButton(
                                onClick = { isFromExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(selectedFrom, fontSize = 12.sp)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                            DropdownMenu(
                                expanded = isFromExpanded,
                                onDismissRequest = { isFromExpanded = false }
                            ) {
                                originOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedFrom = option
                                            isFromExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // To Dropdown
                        Text("TO (Ghat / Temple):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Box {
                            OutlinedButton(
                                onClick = { isToExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(selectedTo, fontSize = 12.sp)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                            DropdownMenu(
                                expanded = isToExpanded,
                                onDismissRequest = { isToExpanded = false }
                            ) {
                                destinationOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedTo = option
                                            isToExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Recommendation Output Card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "SUGGESTED ROUTE:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary
                                )
                                Text(
                                    text = getRouteRecommendation(selectedFrom, selectedTo),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = "• Est. Time: 25 - 40 Mins\n• Fare: Free Kumbh Electric Shuttle\n• Walking: ~500m pedestrian zone near Ramkund",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Route List Title
            item {
                Text(
                    text = "Official Kumbh Shuttle & Transit Routes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Transit Routes
            items(KumbhDataRepository.transportRoutes) { route ->
                RouteCard(route = route)
            }
        }
    }
}

@Composable
private fun RouteCard(route: TransportRoute) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = route.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Mode: ${route.mode}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "⏱ ${route.estimatedTime}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "🎫 ${route.fare}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = route.guidelines, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
        }
    }
}

private fun getRouteRecommendation(from: String, to: String): String {
    return when {
        from.contains("Railway") -> "Take Electric Shuttle Bus #1 directly to Nimani Station -> Walk 600m"
        from.contains("Airport") -> "Board MSRTC Airport Express Coach to Panchavati Gate -> Auto/Shuttle"
        from.contains("Parking") -> "Hop on Free Battery E-Cart #4 along Tapovan Promenade"
        else -> "Use Municipal Special Ring Buses operating every 5 mins"
    }
}
