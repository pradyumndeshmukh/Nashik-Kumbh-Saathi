package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkSaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronPrimary,
    onPrimaryContainer = Color.White,
    secondary = MarigoldYellow,
    onSecondary = Color.Black,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = WarmCream,
    tertiary = DeepTerracotta,
    background = DarkBackground,
    onBackground = WarmCream,
    surface = DarkSurface,
    onSurface = WarmCream,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = WarmCream
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFF3E1200),
    secondary = SaffronLight,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFF421500),
    tertiary = DeepTerracotta,
    onTertiary = Color.White,
    background = WarmCream,
    onBackground = TextDarkSlate,
    surface = SurfaceIvory,
    onSurface = TextDarkSlate,
    surfaceVariant = Color(0xFFFFE0B2),
    onSurfaceVariant = TextDarkSlate
)

@Composable
fun NashikKumbhSaathiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our custom spiritual palette by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
