package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.util.Strings

enum class AppNavTab {
    AJUSTES,
    PERFIL,
    CHATS,
    INTERACCIONES
}

@Composable
fun FloatingGlassBottomBar(
    selectedTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    language: AppLanguage,
    isDark: Boolean,
    unreadChatsCount: Int = 0,
    newInteractionsCount: Int = 0,
    modifier: Modifier = Modifier
) {
    // Left-to-right order so rightmost is INTERACCIONES (first from right):
    // [AJUSTES, PERFIL, CHATS, INTERACCIONES]
    val tabs = listOf(
        NavTabItem(
            tab = AppNavTab.AJUSTES,
            title = Strings.settings(language),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
        NavTabItem(
            tab = AppNavTab.PERFIL,
            title = Strings.profile(language),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.PersonOutline
        ),
        NavTabItem(
            tab = AppNavTab.CHATS,
            title = Strings.chats(language),
            selectedIcon = Icons.Filled.ChatBubble,
            unselectedIcon = Icons.Outlined.ChatBubbleOutline,
            badgeCount = unreadChatsCount
        ),
        NavTabItem(
            tab = AppNavTab.INTERACCIONES,
            title = Strings.interactions(language),
            selectedIcon = Icons.Filled.AutoAwesome,
            unselectedIcon = Icons.Outlined.AutoAwesome,
            badgeCount = newInteractionsCount
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        val capsuleShape = RoundedCornerShape(32.dp)

        // Glass capsule container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = capsuleShape,
                    ambientColor = if (isDark) Color.Black.copy(alpha = 0.5f) else Color(0xFF0284C7).copy(alpha = 0.2f),
                    spotColor = if (isDark) Color.Black.copy(alpha = 0.6f) else Color(0xFF0284C7).copy(alpha = 0.25f)
                )
                .clip(capsuleShape)
                .background(
                    if (isDark) Color(0xFF0F172A).copy(alpha = 0.82f) else Color(0xFFFFFFFF).copy(alpha = 0.86f)
                )
                .border(
                    width = 1.2.dp,
                    brush = Brush.linearGradient(
                        colors = if (isDark) listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.05f),
                            Color(0xFF38BDF8).copy(alpha = 0.3f)
                        ) else listOf(
                            Color.White.copy(alpha = 0.95f),
                            Color.White.copy(alpha = 0.4f),
                            Color(0xFF38BDF8).copy(alpha = 0.35f)
                        )
                    ),
                    shape = capsuleShape
                )
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { item ->
                    val isSelected = selectedTab == item.tab

                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.08f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "tab_scale"
                    )

                    val activeColor = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                    val inactiveColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

                    val tintColor by animateColorAsState(
                        targetValue = if (isSelected) activeColor else inactiveColor,
                        label = "tab_color"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .scale(scale)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = activeColor.copy(alpha = 0.2f)),
                                onClick = { onTabSelected(item.tab) }
                            )
                            .testTag("nav_tab_${item.tab.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(contentAlignment = Alignment.TopEnd) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = tintColor,
                                    modifier = Modifier.size(24.dp)
                                )

                                if (item.badgeCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEF4444))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = item.title,
                                color = tintColor,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class NavTabItem(
    val tab: AppNavTab,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0
)
