package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Friend

@Composable
fun Card3DFriend(
    friend: Friend,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = false,
    unreadCount: Int = 0
) {
    var isPressed by remember { mutableStateOf(false) }
    var tiltX by remember { mutableFloatStateOf(0f) }
    var tiltY by remember { mutableFloatStateOf(0f) }

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val animatedRotationX by animateFloatAsState(
        targetValue = if (isPressed) tiltX else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "rotX"
    )

    val animatedRotationY by animateFloatAsState(
        targetValue = if (isPressed) tiltY else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "rotY"
    )

    val cardCornerRadius = 24.dp
    val badgeCornerRadius = 14.dp

    Column(
        modifier = modifier
            .testTag("friend_card_${friend.id}")
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
                rotationX = animatedRotationX
                rotationY = animatedRotationY
                cameraDistance = 16f * density
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        // Calculate tilt based on touch position
                        tiltX = ((offset.y / size.height) - 0.5f) * -12f
                        tiltY = ((offset.x / size.width) - 0.5f) * 12f
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        tiltX = 0f
                        tiltY = 0f
                    },
                    onTap = {
                        onClick()
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Square 3D Card completely occupied by the profile picture
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(
                    elevation = if (isPressed) 4.dp else 14.dp,
                    shape = RoundedCornerShape(cardCornerRadius),
                    ambientColor = if (isDark) Color.Black.copy(alpha = 0.5f) else Color(0xFF0284C7).copy(alpha = 0.25f),
                    spotColor = if (isDark) Color.Black.copy(alpha = 0.6f) else Color(0xFF0284C7).copy(alpha = 0.35f)
                )
                .clip(RoundedCornerShape(cardCornerRadius))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = if (isDark) listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.05f),
                            Color(0xFF38BDF8).copy(alpha = 0.4f)
                        ) else listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.3f),
                            Color(0xFF38BDF8).copy(alpha = 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(cardCornerRadius)
                )
        ) {
            // Full Avatar Image
            AsyncImage(
                model = friend.avatarUrl,
                contentDescription = friend.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top gradient overlay for contrast with corner badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Vibe Score Badge at Top-Left
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.45f))
                    .border(0.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "${friend.vibeScore}%",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Online Glowing Dot at Top-Right
            if (friend.isOnline) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(14.dp)
                        .background(Color(0xFF22C55E), CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            // Unread Messages Badge at Bottom-Right
            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$unreadCount",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Slim mini-card directly underneath with user's name
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
                    if (isDark) Color(0xFF1E293B).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.9f)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.25f else 0.85f),
                            Color.White.copy(alpha = if (isDark) 0.05f else 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(badgeCornerRadius)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = friend.name,
                    color = if (isDark) Color.White else Color(0xFF0F172A),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (friend.isOnline) "En línea" else friend.statusText,
                    color = if (friend.isOnline) Color(0xFF16A34A) else (if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
