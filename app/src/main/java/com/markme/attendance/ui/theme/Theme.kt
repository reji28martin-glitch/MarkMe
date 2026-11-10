package com.markme.attendance.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val InkTeal = Color(0xFF0B2B2E)      // Deep Ink Teal app bars
val Porcelain = Color(0xFFEDF3F1)    // Background
val LagoonTeal = Color(0xFF0E9F8E)   // Main Accent
val AmberLate = Color(0xFFF59E0B)    // Late Accent
val CoralAbsent = Color(0xFFEF4444)  // Absent/Unknown Accent
val SurfaceWhite = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = LagoonTeal,
    onPrimary = Color.White,
    primaryContainer = InkTeal,
    onPrimaryContainer = Color.White,
    secondary = AmberLate,
    background = Porcelain,
    surface = SurfaceWhite,
    onBackground = InkTeal,
    onSurface = InkTeal,
    error = CoralAbsent
)

@Composable
fun MarkMeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
