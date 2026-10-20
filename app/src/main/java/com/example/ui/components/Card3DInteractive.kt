package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CardType
import com.example.model.CardVisualEffect
import com.example.model.InteractiveCardData

@Composable
fun Card3DInteractive(
    card: InteractiveCardData,
    onVoteOption: (optionId: String) -> Unit,
    onToggleAction: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }
    var tiltX by remember { mutableFloatStateOf(0f) }
    var tiltY by remember { mutableFloatStateOf(0f) }

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )

    val animatedRotX by animateFloatAsState(
        targetValue = if (isPressed) tiltX else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_rotX"
    )

    val animatedRotY by animateFloatAsState(
        targetValue = if (isPressed) tiltY else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_rotY"
    )

    val cardCornerRadius = 24.dp
    val badgeCornerRadius = 14.dp

    val gradientColors = card.effect.gradientColors.map { Color(it) }
    val glowColor = Color(card.effect.glowColor)

    Column(
        modifier = modifier
            .testTag("interactive_card_${card.id}")
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
                rotationX = animatedRotX
                rotationY = animatedRotY
                cameraDistance = 16f * density
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        tiltX = ((offset.y / size.height) - 0.5f) * -10f
                        tiltY = ((offset.x / size.width) - 0.5f) * 10f
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        tiltX = 0f
                        tiltY = 0f
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Square 3D Interactive Card (aspect ratio 1f)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(
                    elevation = if (isPressed) 6.dp else 16.dp,
                    shape = RoundedCornerShape(cardCornerRadius),
                    ambientColor = glowColor.copy(alpha = 0.35f),
                    spotColor = glowColor.copy(alpha = 0.45f)
                )
                .clip(RoundedCornerShape(cardCornerRadius))
                .background(Brush.linearGradient(gradientColors))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.85f),
                            glowColor.copy(alpha = 0.7f),
                            Color.White.copy(alpha = 0.15f)
                        )
                    ),
                    shape = RoundedCornerShape(cardCornerRadius)
                )
                .padding(16.dp)
        ) {
            // Cosmic Singularity Accretion Ring Canvas if COSMIC_SINGULARITY effect
            if (card.effect == CardVisualEffect.COSMIC_SINGULARITY) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    drawCircle(
                        color = Color.White.copy(alpha = 0.08f),
                        radius = size.minDimension * 0.42f,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent),
                            center = center,
                            radius = size.minDimension * 0.35f
                        ),
                        radius = size.minDimension * 0.35f,
                        center = center
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top row with Card Type pill & Sticker Emoji
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .border(0.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when (card.type) {
                            CardType.INVITATION -> Icons.Default.Event
                            CardType.POLL -> Icons.Default.HowToVote
                            CardType.MINI_GAME -> Icons.Default.SportsEsports
                            CardType.VIBE_GREETING -> Icons.Default.CheckCircle
                        }
                        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = card.type.label,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Sticker emoji
                    Text(text = card.stickerEmoji, fontSize = 24.sp)
                }

                // Middle Content
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = card.title,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    if (card.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = card.description,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }

                    if (card.details.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = card.details,
                            color = glowColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Bottom Interactive Action area depending on Card Type
                when (card.type) {
                    CardType.POLL -> {
                        val totalVotes = card.pollOptions.sumOf { it.votes }.coerceAtLeast(1)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            card.pollOptions.take(3).forEach { option ->
                                val isVoted = card.userVotedOptionId == option.id
                                val percent = ((option.votes.toFloat() / totalVotes) * 100).toInt()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.12f))
                                        .border(
                                            1.dp,
                                            if (isVoted) glowColor else Color.White.copy(alpha = 0.2f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { onVoteOption(option.id) }
                                ) {
                                    // Progress bar fill
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(option.votes.toFloat() / totalVotes)
                                            .fillMaxSize()
                                            .background(glowColor.copy(alpha = 0.35f))
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = option.text,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = if (isVoted) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Text(
                                            text = "$percent%",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    CardType.INVITATION -> {
                        Button(
                            onClick = onToggleAction,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (card.isActionCompleted) Color(0xFF22C55E) else glowColor
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                        ) {
                            Text(
                                text = if (card.isActionCompleted) "✓ Asistencia Confirmada" else "Confirmar Asistencia",
                                color = if (card.isActionCompleted) Color.White else Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    CardType.MINI_GAME -> {
                        Button(
                            onClick = onToggleAction,
                            colors = ButtonDefaults.buttonColors(containerColor = glowColor),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                        ) {
                            Text(
                                text = if (card.isActionCompleted) "¡Reto Aceptado! 🏆" else "⚡ Responder Reto",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    CardType.VIBE_GREETING -> {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Konnichiwa Vibe Card ✨", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Slim mini-card directly underneath with user's name (matching user spec)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(badgeCornerRadius),
                    ambientColor = if (isDark) Color.Black.copy(alpha = 0.4f) else Color(0xFF64748B).copy(alpha = 0.15f)
                )
                .clip(RoundedCornerShape(badgeCornerRadius))
                .background(
                    if (isDark) Color(0xFF1E293B).copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = if (isDark) 0.3f else 0.85f),
                            Color.White.copy(alpha = if (isDark) 0.05f else 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(badgeCornerRadius)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = card.creatorName,
                    color = if (isDark) Color.White else Color(0xFF0F172A),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tarjeta ${card.type.label}",
                    color = glowColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
