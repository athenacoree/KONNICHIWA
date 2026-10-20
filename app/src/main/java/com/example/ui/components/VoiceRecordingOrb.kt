package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VoiceRecordingOrbOverlay(
    isRecording: Boolean,
    onCancel: () -> Unit,
    onSend: (durationSeconds: Int) -> Unit,
    isDark: Boolean = true,
    modifier: Modifier = Modifier
) {
    if (!isRecording) return

    var elapsedSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRecording) {
        elapsedSeconds = 0
        while (true) {
            delay(1000)
            elapsedSeconds += 1
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cosmic_orb_anim")

    // Pulsing core scale
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Wave ripples
    val waveProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_progress"
    )

    // Cosmic disc rotation
    val angleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_rotation"
    )

    // Fullscreen Glassmorphism backdrop that hides chat content with frosted blur
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isDark) Color(0xFF030712).copy(alpha = 0.88f)
                else Color(0xFFE0F2FE).copy(alpha = 0.88f)
            )
            .clickable(enabled = false) { } // prevent clicks passing through
    ) {
        // Ambient glass specular overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(40.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFF38BDF8).copy(alpha = if (isDark) 0.15f else 0.25f),
                            Color(0xFFA855F7).copy(alpha = if (isDark) 0.10f else 0.18f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Docked to the BOTTOM of the screen as requested
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Modern glass capsule pill showing recording state & timer
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (isDark) Color(0xFF1E293B).copy(alpha = 0.9f)
                        else Color.White.copy(alpha = 0.9f)
                    )
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(Color(0xFF38BDF8).copy(alpha = 0.7f), Color.White.copy(alpha = 0.3f))),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pulsing red recording dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                )

                Spacer(modifier = Modifier.width(10.dp))

                val minutes = elapsedSeconds / 60
                val seconds = elapsedSeconds % 60
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    color = if (isDark) Color.White else Color(0xFF0F172A),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Grabando nota de voz",
                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // The Jaw-dropping Cosmic Fluid Voice Orb
            Box(
                modifier = Modifier.size(190.dp),
                contentAlignment = Alignment.Center
            ) {
                // Concentric outward event-horizon ripple waves
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val baseRadius = 45.dp.toPx()

                    for (i in 0..2) {
                        val phase = (waveProgress + i * 0.33f) % 1f
                        val waveRadius = baseRadius + phase * 48.dp.toPx()
                        val waveAlpha = ((1f - phase) * 0.55f).coerceIn(0f, 0.55f)

                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF38BDF8).copy(alpha = waveAlpha),
                                    Color(0xFF818CF8).copy(alpha = waveAlpha * 0.5f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = waveRadius
                            ),
                            radius = waveRadius,
                            center = center
                        )

                        drawCircle(
                            color = Color(0xFF38BDF8).copy(alpha = waveAlpha),
                            radius = waveRadius,
                            center = center,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                }

                // Core ambient glow
                Box(
                    modifier = Modifier
                        .size(120.dp * pulseScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFF38BDF8).copy(alpha = 0.8f),
                                    Color(0xFFA855F7).copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                        .blur(14.dp)
                )

                // 3D Cosmic Singularity Fluid Orb
                Canvas(modifier = Modifier.size(96.dp * pulseScale)) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = size.minDimension / 2f

                    val rad = Math.toRadians(angleRotation.toDouble())
                    val offsetX = (cos(rad) * radius * 0.3f).toFloat()
                    val offsetY = (sin(rad) * radius * 0.3f).toFloat()

                    // Cosmic chromatic core
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFFFFF),
                                Color(0xFF38BDF8),
                                Color(0xFF0F172A),
                                Color(0xFF000000)
                            ),
                            center = center + Offset(offsetX, offsetY),
                            radius = radius * 1.15f
                        ),
                        radius = radius,
                        center = center
                    )

                    // Outer glowing glass rim
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF38BDF8),
                                Color(0xFFE879F9),
                                Color(0xFFFFFFFF),
                                Color(0xFF38BDF8)
                            ),
                            center = center
                        ),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                }

                // Inner microphone icon
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action row: Cancel & Send
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel button
                IconButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .size(54.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color(0xFF1E293B).copy(alpha = 0.9f) else Color.White
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar",
                        tint = if (isDark) Color.White else Color(0xFF0F172A),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Suelta o toca para enviar",
                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                // Send voice note button
                IconButton(
                    onClick = {
                        val duration = elapsedSeconds.coerceAtLeast(1)
                        onSend(duration)
                    },
                    modifier = Modifier
                        .size(62.dp)
                        .shadow(12.dp, CircleShape, ambientColor = Color(0xFF38BDF8).copy(alpha = 0.5f))
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF818CF8)))
                        )
                        .border(1.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar Audio",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
