package com.markme.attendance.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markme.attendance.ui.theme.*

/**
 * Main Camera Kiosk Screen with Scanner Frame, Corner Brackets, and Animated Sweep Line
 */
@Composable
fun KioskScreen(
    currentCamera: String,
    onCameraSelect: (String) -> Unit,
    recentMatchBanner: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLine"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InkTeal)
    ) {
        // Camera View Placeholder / Preview Frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Scanner Frame Canvas with Corner Brackets & Neon Line
            Canvas(modifier = Modifier.fillMaxSize(0.85f)) {
                val w = size.width
                val h = size.height
                val cornerLength = 48.dp.toPx()
                val stroke = 4.dp.toPx()
                val bracketColor = LagoonTeal

                // Top-Left
                drawLine(bracketColor, Offset(0f, 0f), Offset(cornerLength, 0f), stroke)
                drawLine(bracketColor, Offset(0f, 0f), Offset(0f, cornerLength), stroke)

                // Top-Right
                drawLine(bracketColor, Offset(w, 0f), Offset(w - cornerLength, 0f), stroke)
                drawLine(bracketColor, Offset(w, 0f), Offset(w, cornerLength), stroke)

                // Bottom-Left
                drawLine(bracketColor, Offset(0f, h), Offset(cornerLength, h), stroke)
                drawLine(bracketColor, Offset(0f, h), Offset(0f, h - cornerLength), stroke)

                // Bottom-Right
                drawLine(bracketColor, Offset(w, h), Offset(w - cornerLength, h), stroke)
                drawLine(bracketColor, Offset(w, h), Offset(w, h - cornerLength), stroke)

                // Sweeping Neon Scan Line
                val currentY = h * scanLineY
                drawLine(
                    color = LagoonTeal.copy(alpha = 0.85f),
                    start = Offset(0f, currentY),
                    end = Offset(w, currentY),
                    strokeWidth = 3.dp.toPx()
                )
            }
        }

        // Top Status Header & Camera Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LagoonTeal, shape = RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("LIVE AI DETECTION", color = Color.White, style = MaterialTheme.typography.labelLarge)
            }
        }

        // Recognition Success Banner
        if (recentMatchBanner != null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                color = LagoonTeal,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(recentMatchBanner, color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
