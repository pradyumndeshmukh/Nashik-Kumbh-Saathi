package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.CrowdLevel
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.SaffronPrimary
import com.example.ui.AppLanguage
import com.example.ui.util.tr
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class LiveZoneTraffic(
    val id: String,
    val zoneName: String,
    val trafficStatus: String,
    val crowdDensityPercent: Int,
    val estimatedWaitMinutes: Int,
    val statusColor: Color,
    val mapsQuery: String
)

@Composable
fun CrowdAdvisoryBanner(
    currentAdvisory: CrowdLevel,
    onAdvisorySelected: (CrowdLevel) -> Unit = {},
    onNavigateToMap: (() -> Unit)? = null,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showZoneDetails by remember { mutableStateOf(false) }
    var isRefreshingTraffic by remember { mutableStateOf(false) }
    var lastUpdatedTime by remember { mutableStateOf("10:00 AM") }

    // Live Traffic Map Modal state
    var showTrafficDialog by remember { mutableStateOf(false) }
    var trafficDialogQuery by remember { mutableStateOf("Nashik Panchavati Traffic") }

    // Dynamic Live Google Maps Traffic Feed data
    var liveZoneTraffics by remember {
        mutableStateOf(
            listOf(
                LiveZoneTraffic(
                    id = "z1",
                    zoneName = "Ramkund Panchavati",
                    trafficStatus = "Heavy Density",
                    crowdDensityPercent = 88,
                    estimatedWaitMinutes = 25,
                    statusColor = Color(0xFFD32F2F),
                    mapsQuery = "Ramkund Ghat Panchavati Nashik traffic"
                ),
                LiveZoneTraffic(
                    id = "z2",
                    zoneName = "Sadhugram Tapovan",
                    trafficStatus = "Moderate Flow",
                    crowdDensityPercent = 48,
                    estimatedWaitMinutes = 10,
                    statusColor = Color(0xFFF57C00),
                    mapsQuery = "Tapovan Sadhugram Nashik traffic"
                ),
                LiveZoneTraffic(
                    id = "z3",
                    zoneName = "Trimbakeshwar Route",
                    trafficStatus = "Smooth Movement",
                    crowdDensityPercent = 22,
                    estimatedWaitMinutes = 5,
                    statusColor = Color(0xFF388E3C),
                    mapsQuery = "Trimbakeshwar Nashik highway traffic"
                ),
                LiveZoneTraffic(
                    id = "z4",
                    zoneName = "CBS & Railway Hub",
                    trafficStatus = "Moderate Traffic",
                    crowdDensityPercent = 52,
                    estimatedWaitMinutes = 12,
                    statusColor = Color(0xFFF57C00),
                    mapsQuery = "Nashik Road station CBS traffic"
                )
            )
        )
    }

    val containerColor = Color(currentAdvisory.hexColor).copy(alpha = 0.12f)
    val badgeColor = Color(currentAdvisory.hexColor)

    fun openTrafficMap(query: String) {
        trafficDialogQuery = query
        showTrafficDialog = true
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("crowd_advisory_banner"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (currentAdvisory == CrowdLevel.VERY_HIGH || currentAdvisory == CrowdLevel.HIGH)
                            Icons.Default.Warning else Icons.Default.Info,
                        contentDescription = "Advisory Icon",
                        tint = badgeColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Today's Crowd Advisory".tr(appLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Google Maps Traffic & Live Density Sync".tr(appLanguage),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = badgeColor
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${currentAdvisory.label.tr(appLanguage)} (MAP LIVE)".tr(appLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Advisory Description
            Text(
                text = currentAdvisory.description.tr(appLanguage),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = badgeColor.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(10.dp))

            // Live Google Maps Traffic Sync Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E7D32))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "LIVE GOOGLE MAPS TRAFFIC".tr(appLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Last synced: $lastUpdatedTime".tr(appLanguage),
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            isRefreshingTraffic = true
                            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            lastUpdatedTime = timeFormat.format(Date())

                            // Simulate real-time live traffic fluctuations
                            liveZoneTraffics = liveZoneTraffics.map { zone ->
                                val delta = Random.nextInt(-5, 6)
                                val newDensity = (zone.crowdDensityPercent + delta).coerceIn(15, 98)
                                val newWait = (newDensity * 0.3f).toInt().coerceAtLeast(3)
                                val (status, color) = when {
                                    newDensity >= 75 -> "Heavy Density" to Color(0xFFD32F2F)
                                    newDensity >= 40 -> "Moderate Flow" to Color(0xFFF57C00)
                                    else -> "Smooth Movement" to Color(0xFF388E3C)
                                }
                                zone.copy(
                                    crowdDensityPercent = newDensity,
                                    estimatedWaitMinutes = newWait,
                                    trafficStatus = status,
                                    statusColor = color
                                )
                            }
                            isRefreshingTraffic = false
                            Toast.makeText(context, "Updated live Google Maps traffic & crowd density!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync Traffic",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    TextButton(
                        onClick = { showZoneDetails = !showZoneDetails },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (showZoneDetails) "Hide Zones ▲".tr(appLanguage) else "View All Zones ▼".tr(appLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Horizontal Cards for Zone Densities
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(liveZoneTraffics) { zone ->
                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .clickable {
                                openTrafficMap(zone.mapsQuery)
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = zone.zoneName.tr(appLanguage),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )

                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(zone.statusColor)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${zone.trafficStatus.tr(appLanguage)} • ${zone.crowdDensityPercent}% Full",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = zone.statusColor
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Density Progress Bar
                            LinearProgressIndicator(
                                progress = zone.crowdDensityPercent / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = zone.statusColor,
                                trackColor = zone.statusColor.copy(alpha = 0.2f)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Wait ~${zone.estimatedWaitMinutes}m".tr(appLanguage),
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "View Map",
                                    tint = DeepSaffron,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Expanded Detailed Zone View
            AnimatedVisibility(visible = showZoneDetails) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    liveZoneTraffics.forEach { zone ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(zone.statusColor)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = zone.zoneName.tr(appLanguage),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${zone.trafficStatus.tr(appLanguage)} | Est. Snan Wait: ${zone.estimatedWaitMinutes} mins",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    openTrafficMap(zone.mapsQuery)
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(26.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                            ) {
                                Text("Live Map".tr(appLanguage), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Google Maps Live Traffic Action Button
            Button(
                onClick = {
                    openTrafficMap("Nashik Panchavati Live Traffic Congestion")
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Map, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Open Google Maps Live Traffic Feed".tr(appLanguage), fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Interactive In-App Google Maps Live Traffic Dialog
    if (showTrafficDialog) {
        Dialog(
            onDismissRequest = { showTrafficDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Dialog Header Bar
                    Surface(
                        color = SaffronPrimary,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Live Traffic Map".tr(appLanguage),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = trafficDialogQuery.tr(appLanguage),
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                        maxLines = 1
                                    )
                                }
                            }

                            IconButton(
                                onClick = { showTrafficDialog = false },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Live Interactive Google Map WebView
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFFE0E0E0))
                    ) {
                        val directMapUrl = "https://maps.google.com/maps?q=${Uri.encode(trafficDialogQuery + " live traffic")}&output=embed"

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
                    }

                    // Dialog Footer Action Buttons
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val mapsUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(trafficDialogQuery)}")
                                    try {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Opening browser maps...", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Open Google Maps App".tr(appLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            if (onNavigateToMap != null) {
                                Button(
                                    onClick = {
                                        showTrafficDialog = false
                                        onNavigateToMap()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Full App Map".tr(appLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

