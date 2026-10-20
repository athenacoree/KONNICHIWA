package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.WallpaperCatalog
import com.example.ui.AppViewModel
import com.example.ui.components.AppNavTab
import com.example.ui.components.FloatingGlassBottomBar
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.ChatsScreen
import com.example.ui.screens.InteraccionesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ThreadsScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by viewModel.settings.collectAsState()
            val wallpaper = WallpaperCatalog.findWallpaper(settings.style.selectedWallpaperId)
            val isDark = settings.style.isDarkMode

            MyApplicationTheme(darkTheme = isDark, dynamicColor = false) {
                // Dynamic Wallpaper Backdrop
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(wallpaper.topColor),
                                    Color(wallpaper.bottomColor)
                                )
                            )
                        )
                ) {
                    // Soft Ambient Glass Glow Spheres for iOS frosted glass feel
                    AmbientGlassGlowOrbs(isDark = isDark, accentColor = Color(wallpaper.accentGlow))

                    MainAppContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: AppViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeChatFriend by viewModel.activeChatFriend.collectAsState()
    val activeThreadMessage by viewModel.activeThreadMessage.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val messagesByFriend by viewModel.messagesByFriend.collectAsState()
    val interactions by viewModel.interactions.collectAsState()
    val currentUserProfile by viewModel.currentUserProfile.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val isDark = settings.style.isDarkMode
    val language = settings.language

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            // Thread View has highest priority when opened
            activeThreadMessage != null -> {
                ThreadsScreen(
                    parentMessage = activeThreadMessage!!,
                    onBack = { viewModel.closeThread() },
                    onSendReply = { replyText ->
                        viewModel.addThreadReply(activeThreadMessage!!.id, replyText)
                    },
                    isDark = isDark,
                    language = language
                )
            }

            // Chat Detail View
            activeChatFriend != null -> {
                val currentFriend = activeChatFriend!!
                val currentMessages = messagesByFriend[currentFriend.id].orEmpty()

                ChatDetailScreen(
                    friend = currentFriend,
                    messages = currentMessages,
                    onBack = { viewModel.closeChat() },
                    onSendMessage = { text, mediaUrl ->
                        viewModel.sendMessage(text, mediaUrl)
                    },
                    onSendCard = { card ->
                        viewModel.sendInteractiveCard(card)
                    },
                    onVoiceSend = { durationSeconds ->
                        viewModel.sendVoiceRecording(durationSeconds)
                    },
                    onOpenThread = { message ->
                        viewModel.openThread(message)
                    },
                    onToggleReaction = { messageId, emoji ->
                        viewModel.toggleReaction(messageId, emoji)
                    },
                    onVoteOption = { messageId, optionId ->
                        viewModel.voteOnPoll(messageId, optionId)
                    },
                    onToggleCardAction = { messageId ->
                        viewModel.toggleCardAction(messageId)
                    },
                    userName = currentUserProfile.name,
                    isDark = isDark,
                    language = language
                )
            }

            // Main Tab Screens with Floating Glass Bottom Bar
            else -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tab_transition"
                    ) { tab ->
                        when (tab) {
                            AppNavTab.INTERACCIONES -> {
                                InteraccionesScreen(
                                    interactions = interactions,
                                    onFriendClick = { friend ->
                                        viewModel.openChat(friend)
                                    },
                                    onSimulateVibe = {
                                        viewModel.simulateIncomingVibe()
                                    },
                                    isDark = isDark
                                )
                            }

                            AppNavTab.CHATS -> {
                                ChatsScreen(
                                    friends = friends,
                                    onFriendClick = { friend ->
                                        viewModel.openChat(friend)
                                    },
                                    onAddFriend = { name, username, bio, avatarUrl ->
                                        viewModel.addFriend(name, username, bio, avatarUrl)
                                    },
                                    isDark = isDark,
                                    language = language
                                )
                            }

                            AppNavTab.PERFIL -> {
                                ProfileScreen(
                                    userProfile = currentUserProfile,
                                    onUpdateProfile = { name, username, bio, mood, avatar ->
                                        viewModel.updateProfile(name, username, bio, mood, avatar)
                                    },
                                    isDark = isDark,
                                    language = language
                                )
                            }

                            AppNavTab.AJUSTES -> {
                                SettingsScreen(
                                    settings = settings,
                                    onUpdateSettings = { newSettings ->
                                        viewModel.updateSettings(newSettings)
                                    },
                                    onSelectWallpaper = { id, isDarkTheme ->
                                        viewModel.selectWallpaper(id, isDarkTheme)
                                    },
                                    onSelectLanguage = { lang ->
                                        viewModel.setLanguage(lang)
                                    },
                                    isDark = isDark
                                )
                            }
                        }
                    }

                    // Floating Glass Bottom Navigation Bar (WhatsApp right-to-left layout)
                    FloatingGlassBottomBar(
                        selectedTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        language = language,
                        isDark = isDark,
                        unreadChatsCount = 1,
                        newInteractionsCount = 2,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
fun AmbientGlassGlowOrbs(isDark: Boolean, accentColor: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top right ambient orb
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = 100.dp, y = (-60).dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        listOf(
                            accentColor.copy(alpha = if (isDark) 0.18f else 0.28f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(48.dp)
        )

        // Bottom left ambient orb
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = (-80).dp, y = 140.dp)
                .align(Alignment.BottomStart)
                .background(
                    Brush.radialGradient(
                        listOf(
                            (if (isDark) Color(0xFF818CF8) else Color(0xFFBAE6FD)).copy(alpha = if (isDark) 0.14f else 0.35f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(56.dp)
        )
    }
}
