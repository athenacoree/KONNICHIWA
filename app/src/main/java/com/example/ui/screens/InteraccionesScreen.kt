package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Friend
import com.example.model.InteractionEvent
import com.example.model.MessageType
import com.example.ui.components.YouTubeEmbedPlayer

@Composable
fun InteraccionesScreen(
    interactions: List<InteractionEvent>,
    onFriendClick: (Friend) -> Unit,
    onSimulateVibe: () -> Unit,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    Box(modifier = modifier.fillMaxSize()) {
        // Pure freeform list without boxed frames or unnecessary headers/explanations
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("interactions_list"),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 56.dp, // below status bar
                bottom = 110.dp // above floating bottom bar
            ),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            items(
                items = interactions,
                key = { it.id }
            ) { event ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically { it / 2 }
                ) {
                    FreeformInteractionItem(
                        event = event,
                        isDark = isDark,
                        textColor = textColor,
                        subTextColor = subTextColor,
                        onFriendClick = { onFriendClick(event.friend) }
                    )
                }
            }
        }

        // Sleek subtle live simulator button at top right
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 20.dp, top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.7f))
                    .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSimulateVibe
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Live Vibe",
                        tint = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Vibe +",
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun FreeformInteractionItem(
    event: InteractionEvent,
    isDark: Boolean,
    textColor: Color,
    subTextColor: Color,
    onFriendClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onFriendClick
            )
            .testTag("interaction_item_${event.id}")
    ) {
        // 1. FIRST: The message content itself (text, image, audio, or YouTube)
        // Completely free-floating, without enclosing card frames
        when (event.message.type) {
            MessageType.TEXT -> {
                Text(
                    text = event.message.text,
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 30.sp,
                    letterSpacing = (-0.3).sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            MessageType.IMAGE -> {
                Column {
                    if (event.message.text.isNotBlank() && event.message.text != "Caminando entre la niebla del atardecer 🌫️") {
                        Text(
                            text = event.message.text,
                            color = textColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 28.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    // Floating seamless photo with delicate rounded corners & glass border
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .shadow(12.dp, RoundedCornerShape(26.dp), ambientColor = Color.Black.copy(alpha = 0.2f))
                            .clip(RoundedCornerShape(26.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(26.dp))
                    ) {
                        AsyncImage(
                            model = event.message.mediaUrl,
                            contentDescription = "Shared photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    if (event.message.text == "Caminando entre la niebla del atardecer 🌫️") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = event.message.text,
                            color = textColor,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            MessageType.AUDIO -> {
                // Audio note: Floating waveform player
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isDark) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.6f))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Audio",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Nota de voz",
                                color = textColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${event.message.audioDurationSeconds}s",
                                color = subTextColor,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Waveform bars
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val bars = if (event.message.audioWaveform.isNotEmpty()) {
                                event.message.audioWaveform
                            } else {
                                listOf(0.3f, 0.6f, 0.8f, 0.4f, 0.9f, 0.7f, 0.5f, 0.85f, 0.4f, 0.6f)
                            }
                            bars.forEach { heightRatio ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height((heightRatio * 22).coerceAtLeast(4f).dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7))
                                )
                            }
                        }
                    }
                }
            }

            MessageType.YOUTUBE -> {
                // Official YouTube video inline without buttons
                Column {
                    if (event.message.youtubeVideoId != null) {
                        YouTubeEmbedPlayer(
                            videoId = event.message.youtubeVideoId,
                            isDark = isDark
                        )
                    } else {
                        Text(
                            text = event.message.text,
                            color = textColor,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            MessageType.CARD -> {
                if (event.message.cardData != null) {
                    Box(modifier = Modifier.fillMaxWidth(0.92f)) {
                        com.example.ui.components.Card3DInteractive(
                            card = event.message.cardData,
                            onVoteOption = { },
                            onToggleAction = { },
                            isDark = isDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. DIRECTLY UNDERNEATH: Who sent it (Avatar + Name) and the time
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Friend avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                ) {
                    AsyncImage(
                        model = event.friend.avatarUrl,
                        contentDescription = event.friend.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Name and Time (e.g. Camila Ortiz • hace 2 min)
                Text(
                    text = "${event.friend.name}  •  ${event.timeAgo}",
                    color = subTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Quick glass like reaction
            IconButton(
                onClick = { isLiked = !isLiked },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) Color(0xFFEF4444) else subTextColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
