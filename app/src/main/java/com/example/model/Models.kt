package com.example.model

enum class AppLanguage(val displayName: String, val code: String) {
    SPANISH("Español", "es"),
    ENGLISH("English", "en"),
    JAPANESE("日本語", "ja")
}

data class WallpaperTheme(
    val id: String,
    val nameEs: String,
    val nameEn: String,
    val nameJa: String,
    val isDark: Boolean,
    val topColor: Long,
    val bottomColor: Long,
    val accentGlow: Long,
    val isLightText: Boolean
) {
    fun localizedName(language: AppLanguage): String = when (language) {
        AppLanguage.SPANISH -> nameEs
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.JAPANESE -> nameJa
    }
}

enum class MessageType {
    TEXT,
    AUDIO,
    IMAGE,
    YOUTUBE,
    CARD
}

enum class CardType(val label: String) {
    INVITATION("Invitación VIP"),
    VIBE_GREETING("Saludo Libre"),
    POLL("Encuesta"),
    MINI_GAME("Mini-Juego")
}

enum class CardVisualEffect(val displayName: String, val gradientColors: List<Long>, val glowColor: Long) {
    COSMIC_SINGULARITY(
        "Singularidad Cósmica",
        listOf(0xFF000000, 0xFF0F172A, 0xFF1E293B),
        0xFFE2E8F0
    ),
    PURE_GLASS_PRISM(
        "Cristal Prisma",
        listOf(0xEEFFFFFF, 0xDDCCE5FF, 0xEEDBEAFE),
        0xFF38BDF8
    ),
    CYBER_NEON(
        "Neón Cyberpunk",
        listOf(0xFF2E0854, 0xFF18032E, 0xFF0A0114),
        0xFFE879F9
    ),
    HOLOGRAPHIC_GOLD(
        "Oro Holográfico",
        listOf(0xFF422006, 0xFF1F1004, 0xFF0D0601),
        0xFFFBBF24
    ),
    AURORA_BOREALIS(
        "Aurora Boreal",
        listOf(0xFF064E3B, 0xFF022C22, 0xFF011A14),
        0xFF34D399
    ),
    SAKURA_GLOW(
        "Sakura Glow",
        listOf(0xFF500724, 0xFF2A0312, 0xFF140108),
        0xFFF472B6
    )
}

data class PollOption(
    val id: String,
    val text: String,
    val votes: Int = 0
)

data class InteractiveCardData(
    val id: String,
    val type: CardType,
    val effect: CardVisualEffect = CardVisualEffect.COSMIC_SINGULARITY,
    val title: String,
    val description: String = "",
    val details: String = "", // Date, location, or note
    val stickerEmoji: String = "✨",
    val pollOptions: List<PollOption> = emptyList(),
    val userVotedOptionId: String? = null,
    val isActionCompleted: Boolean = false,
    val creatorName: String = "Alex Rivera"
)

data class ThreadReply(
    val id: String,
    val senderName: String,
    val senderAvatar: String,
    val text: String,
    val time: String,
    val isMe: Boolean
)

data class Message(
    val id: String,
    val senderId: String,
    val recipientId: String,
    val text: String,
    val type: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null,
    val audioDurationSeconds: Int = 0,
    val audioWaveform: List<Float> = emptyList(),
    val youtubeVideoId: String? = null,
    val cardData: InteractiveCardData? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val formattedTime: String = "12:00",
    val isRead: Boolean = true,
    val threadReplies: List<ThreadReply> = emptyList(),
    val reactions: Map<String, Int> = emptyMap()
)

data class Friend(
    val id: String,
    val name: String,
    val username: String,
    val avatarUrl: String,
    val statusText: String,
    val isOnline: Boolean,
    val lastSeenText: String,
    val bio: String,
    val vibeScore: Int = 95,
    val sharedMemoriesCount: Int = 12
)

data class InteractionEvent(
    val id: String,
    val friend: Friend,
    val message: Message,
    val timeAgo: String,
    val isUnread: Boolean = false
)

data class UserProfile(
    val name: String = "Alex Rivera",
    val username: String = "alex_konnichiwa",
    val avatarUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop&q=80",
    val bio: String = "Konnichiwa! 🌌 Viviendo entre diseño y música cósmica ✨",
    val statusMood: String = "🎧 Escuchando Daft Punk",
    val vibeScore: Int = 99,
    val friendsCount: Int = 8,
    val threadsCount: Int = 34,
    val highlights: List<String> = listOf("Singularity 🪐", "Studio Beats 🎹", "Tokyo Drift 🗼", "Night Talks ☕")
)
