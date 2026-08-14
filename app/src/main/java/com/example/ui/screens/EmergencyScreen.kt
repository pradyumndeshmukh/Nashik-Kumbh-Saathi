package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KumbhDataRepository
import com.example.ui.LostAndFoundReport
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr

@Composable
fun EmergencyScreen(
    lostReports: List<LostAndFoundReport>,
    onSubmitLostReport: (LostAndFoundReport) -> Unit,
    onShowToast: (String) -> Unit,
    onNavigateToMedicals: (() -> Unit)? = null,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showForm by remember { mutableStateOf(false) }

    // Form State
    var reportType by remember { mutableStateOf("Missing Person") }
    var nameInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var descInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("emergency_screen")
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFC62828)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Emergency, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Emergency & Safety Desk".tr(appLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("1-Tap Police, Medical SOS & Lost-and-Found".tr(appLanguage), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Urgent 1-Tap SOS Buttons Grid
            item {
                Text("Urgent Helplines (1-Tap Call)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        SosButton(
                            title = "POLICE",
                            number = "112",
                            color = Color(0xFF1565C0),
                            icon = Icons.Default.LocalPolice,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                                context.startActivity(intent)
                            }
                        )
                        SosButton(
                            title = "AMBULANCE",
                            number = "108",
                            color = Color(0xFFC62828),
                            icon = Icons.Default.LocalHospital,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                                context.startActivity(intent)
                            }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        SosButton(
                            title = "FIRE",
                            number = "101",
                            color = Color(0xFFE65100),
                            icon = Icons.Default.LocalFireDepartment,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:101"))
                                context.startActivity(intent)
                            }
                        )
                        SosButton(
                            title = "LOST & FOUND",
                            number = "1800-233-1122",
                            color = Color(0xFF2E7D32),
                            icon = Icons.Default.PersonSearch,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18002331122"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }

            // Nearby Hospitals & Medicals Quick Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToMedicals?.invoke() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFC62828)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    tint = Color(0xFFC62828),
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Medicals & Nearby Hospitals",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Ramkund, Trimbak, Station, CBS, Nimani & All Hubs",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Lost & Found Registration Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Lost & Found Desk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Register missing child, elder, or lost baggage", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Button(
                                onClick = { showForm = !showForm },
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                            ) {
                                Text(if (showForm) "Close" else "+ Report")
                            }
                        }

                        AnimatedVisibility(visible = showForm) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                Text("Report Type:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    FilterChip(
                                        selected = reportType == "Missing Person",
                                        onClick = { reportType = "Missing Person" },
                                        label = { Text("Missing Person") }
                                    )
                                    FilterChip(
                                        selected = reportType == "Lost Item",
                                        onClick = { reportType = "Lost Item" },
                                        label = { Text("Lost Item/Bag") }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = nameInput,
                                    onValueChange = { nameInput = it },
                                    label = { Text("Name of Person / Item") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = locationInput,
                                    onValueChange = { locationInput = it },
                                    label = { Text("Last Seen Location (e.g. Ramkund Stairs)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = phoneInput,
                                    onValueChange = { phoneInput = it },
                                    label = { Text("Contact Phone Number") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = descInput,
                                    onValueChange = { descInput = it },
                                    label = { Text("Clothes, Age, Photo or Item Description") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (nameInput.isNotBlank() && phoneInput.isNotBlank()) {
                                            onSubmitLostReport(
                                                LostAndFoundReport(
                                                    personOrItemName = nameInput,
                                                    reportType = reportType,
                                                    lastSeenLocation = locationInput,
                                                    contactPhone = phoneInput,
                                                    description = descInput
                                                )
                                            )
                                            nameInput = ""
                                            locationInput = ""
                                            phoneInput = ""
                                            descInput = ""
                                            showForm = false
                                            onShowToast("Lost & Found report submitted to Police Control Desk.")
                                        } else {
                                            onShowToast("Please enter name and contact phone number.")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                                ) {
                                    Text("Submit Report to Control Desk")
                                }
                            }
                        }
                    }
                }
            }

            // Recent Active Reports
            item {
                Text("Recent Lost & Found Announcements", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            items(lostReports) { report ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = report.personOrItemName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = report.reportType,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(text = "Last seen: ${report.lastSeenLocation} • ${report.timestamp}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = report.description, fontSize = 11.sp, modifier = Modifier.padding(vertical = 4.dp))
                        Text(text = "📞 Contact: ${report.contactPhone}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                }
            }

            // Crowd & Health Advisories
            item {
                Text("Pilgrim Health & Safety Advisories", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            items(KumbhDataRepository.healthAdvisories) { advisory ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = advisory.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = advisory.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun SosButton(
    title: String,
    number: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = number, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}
