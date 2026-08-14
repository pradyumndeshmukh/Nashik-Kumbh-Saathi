package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun AnimatedKumbhLogo(
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    showHalo: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "kumbh_logo_anim")

    // Pulse scale animation
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Halo expansion animation
    val haloScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_scale"
    )

    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_alpha"
    )

    // Gentle pendulum sway animation
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pendulum_sway"
    )

    Box(
        modifier = modifier
            .size(size * 1.3f),
        contentAlignment = Alignment.Center
    ) {
        // Outer glowing radiating halo
        if (showHalo) {
            Box(
                modifier = Modifier
                    .size(size)
                    .scale(haloScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFF9800).copy(alpha = haloAlpha),
                                Color(0xFFE65100).copy(alpha = haloAlpha * 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // Main Animated Logo Container
        Box(
            modifier = Modifier
                .size(size)
                .scale(pulseScale)
                .rotate(rotationAngle)
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    width = 2.5.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFFE65100),
                            Color(0xFFFFB300),
                            Color(0xFF0288D1),
                            Color(0xFFE65100)
                        )
                    ),
                    shape = CircleShape
                )
                .then(
                    if (onClick != null) Modifier.clickable { onClick() } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_kumbh_logo),
                contentDescription = "Simhastha Kumbh Mela Sacred Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
