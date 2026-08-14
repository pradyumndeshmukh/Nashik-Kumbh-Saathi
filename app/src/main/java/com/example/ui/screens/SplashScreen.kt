package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedKumbhLogo
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Animation triggers
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1000) // 1.0 second splash screen duration for smooth logo animation & fast startup
        onSplashFinished()
    }

    // Scale animation for central logo
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.2f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    // Unfolding Rotation spin animation for central logo on startup
    val logoRotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -180f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "logo_rotation"
    )

    // Alpha animation for central logo
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing),
        label = "logo_alpha"
    )

    // Alpha animation for text elements
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300, easing = FastOutSlowInEasing),
        label = "text_alpha"
    )

    // Translation Y animation for title
    val titleOffsetY by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 40.dp,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300, easing = FastOutSlowInEasing),
        label = "title_offset"
    )

    // Background radiating pulse ring scale
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_rings")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_scale"
    )

    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onSplashFinished() }
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3E1200), // Rich deep burgundy-saffron night
                        DeepSaffron,
                        SaffronPrimary,
                        Color(0xFFE65100)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Radiating golden background ring
        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(ringScale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GoldAccent.copy(alpha = ringAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Central Logo with Spring scale, rotation spin & alpha entrance animation
            Box(
                modifier = Modifier
                    .scale(logoScale)
                    .rotate(logoRotation)
                    .alpha(logoAlpha),
                contentAlignment = Alignment.Center
            ) {
                AnimatedKumbhLogo(
                    size = 140.dp,
                    showHalo = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Animated Text Container
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = titleOffsetY)
            ) {
                Text(
                    text = "कुंभ साथी 2027",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Nashik - Trimbakeshwar Simhastha",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Official Holy Pilgrimage Companion",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Sacred Blessing Pill
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(50.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "ॐ नमः शिवाय • Jai Godavari Maiya",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = GoldAccent
                        )
                    }
                }
            }
        }

        // Bottom Loading Indicator / Copyright
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .alpha(textAlpha)
        ) {
            Text(
                text = "Loading Sacred Guides...",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}
