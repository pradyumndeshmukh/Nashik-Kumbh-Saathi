package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.KumbhDataRepository
import com.example.ui.theme.SaffronPrimary

import com.example.ui.AppLanguage
import com.example.ui.util.tr
import androidx.compose.material.icons.filled.Menu

@Composable
fun CultureScreen(
    onBack: () -> Unit,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("culture_screen")
    ) {
        // Top App Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onOpenDrawer) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = SaffronPrimary)
                }
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text("Info & Cultural Heritage".tr(appLanguage), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Simhastha Traditions, Akharas & Sacred Ghat Rules".tr(appLanguage), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Intro Card with Hero Picture
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.img_culture_shahi_snan),
                            contentDescription = "Simhastha Kumbh Shahi Snan",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Nashik-Trimbak Simhastha 2027", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("A sacred gathering occurring once every 12 celestial years on the holy Godavari banks.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Culture Topics with Dedicated Illustrations
            items(KumbhDataRepository.cultureTopics) { topic ->
                val imageRes = getCultureImageRes(topic.id)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = topic.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = topic.title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = topic.summary, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = topic.detailedText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
                        }
                    }
                }
            }

            // Quick Do's and Don'ts Card with Ramkund Picture
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.img_culture_ramkund),
                            contentDescription = "Ramkund Sacred Ghat Etiquette",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Ghat Rules: Do's and Don'ts", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(10.dp))

                            RuleItem(isDo = true, text = "Wear modest traditional attire while visiting ghats and temples.")
                            RuleItem(isDo = true, text = "Use free RFID wristbands for elders and children at Police Booths.")
                            RuleItem(isDo = true, text = "Drink water regularly at free RO purified kiosks.")

                            Spacer(modifier = Modifier.height(8.dp))

                            RuleItem(isDo = false, text = "Do NOT use soap, shampoo, or plastic bags in River Godavari.")
                            RuleItem(isDo = false, text = "Do NOT push or rush on pontoon bridges during peak Shahi Snan hours.")
                            RuleItem(isDo = false, text = "Do NOT leave trash or litter on riverbank promenades.")
                        }
                    }
                }
            }
        }
    }
}

private fun getCultureImageRes(topicId: String): Int {
    return when (topicId) {
        "ct_1" -> R.drawable.img_culture_simhastha
        "ct_2" -> R.drawable.img_culture_shahi_snan
        "ct_3" -> R.drawable.img_culture_akharas
        "ct_4" -> R.drawable.img_culture_ramkund
        else -> R.drawable.img_culture_ramkund
    }
}

@Composable
private fun RuleItem(isDo: Boolean, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isDo) Icons.Default.CheckCircle else Icons.Default.DoNotDisturb,
            contentDescription = null,
            tint = if (isDo) Color(0xFF2E7D32) else Color(0xFFC62828),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
