package com.example.model

data class AccessibilitySettings(
    val dynamicTextScale: Float = 1.0f,
    val highContrastText: Boolean = false,
    val reduceMotion: Boolean = false,
    val hapticFeedback: Boolean = true,
    val chatSoundEffects: Boolean = true,
    val autoVoiceTranscription: Boolean = true,
    val autoVideoSubtitles: Boolean = false,
    val screenReaderOptimized: Boolean = false,
    val largeTouchTargets: Boolean = true,
    val longPressDurationMs: Int = 300,
    val colorBlindFilter: String = "Normal" // Normal, Protanopia, Deuteranopia, Tritanopia
)

data class PrivacySettings(
    val lastSeenVisibility: String = "Amigos", // Todos, Amigos, Nadie
    val readReceipts: Boolean = true,
    val hideOnlineStatus: Boolean = false,
    val ephemeralMedia: Boolean = false,
    val threadsVisibility: String = "Solo Amigos",
    val screenshotAlerts: Boolean = true,
    val incognitoVoiceListening: Boolean = true,
    val autoDeleteChatDays: Int = 0, // 0 = Nunca, 1, 7, 30
    val hideNotificationPreview: Boolean = false,
    val hideYouTubeWatchStatus: Boolean = false,
    val allowFriendVibeSync: Boolean = true
)

data class SecuritySettings(
    val biometricLockEnabled: Boolean = false,
    val pinProtectionEnabled: Boolean = false,
    val pinCode: String = "",
    val endToEndEncryptionActive: Boolean = true,
    val antiPhishingShield: Boolean = true,
    val suspiciousLoginAlerts: Boolean = true,
    val twoFactorAuth: Boolean = false,
    val selfDestructOnExit: Boolean = false,
    val safeLinkSandbox: Boolean = true,
    val panicEmergencyWipeShortcut: Boolean = false,
    val encryptedCloudBackups: Boolean = true
)

data class StyleSettings(
    val isDarkMode: Boolean = false,
    val selectedWallpaperId: String = "light_aurora_sky",
    val glassBlurIntensity: Float = 0.85f,
    val iridescentBorderGlow: Boolean = true,
    val card3DTiltEffect: Boolean = true,
    val bubbleStyle: String = "Telegram-Instagram", // "Telegram-Instagram", "Pure Glass", "Minimal"
    val cardCornerRadiusDp: Int = 24,
    val accentColorTheme: String = "Neon Sky", // "Neon Sky", "Sakura Pink", "Emerald", "Amethyst", "Sunset Gold"
    val voiceOrbHologramStyle: String = "ChatGPT Fluid", // "ChatGPT Fluid", "Siri Wave", "Neon Pulse"
    val showTypingAura: Boolean = true,
    val interactionFloatingSpeed: Float = 1.0f
)

data class FullAppSettings(
    val language: AppLanguage = AppLanguage.SPANISH,
    val accessibility: AccessibilitySettings = AccessibilitySettings(),
    val privacy: PrivacySettings = PrivacySettings(),
    val security: SecuritySettings = SecuritySettings(),
    val style: StyleSettings = StyleSettings()
)

object WallpaperCatalog {
    val lightWallpapers = listOf(
        WallpaperTheme(
            id = "light_aurora_sky",
            nameEs = "Cielo Cristalino (Predeterminado)",
            nameEn = "Crystal Sky (Default)",
            nameJa = "クリスタルスカイ (デフォルト)",
            isDark = false,
            topColor = 0xFFD8EEFF,
            bottomColor = 0xFFFFFFFF,
            accentGlow = 0xFF38BDF8,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_frosted_pearl",
            nameEs = "Perla Helada",
            nameEn = "Frosted Pearl",
            nameJa = "フロストパール",
            isDark = false,
            topColor = 0xFFF1F5F9,
            bottomColor = 0xFFFAFAFA,
            accentGlow = 0xFF94A3B8,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_pastel_sakura",
            nameEs = "Sakura Suave",
            nameEn = "Soft Sakura",
            nameJa = "桜のそよ風",
            isDark = false,
            topColor = 0xFFFCE7F3,
            bottomColor = 0xFFFFF1F2,
            accentGlow = 0xFFF472B6,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_mint_breeze",
            nameEs = "Brisa de Menta",
            nameEn = "Mint Breeze",
            nameJa = "ミントブリーズ",
            isDark = false,
            topColor = 0xFFD1FAE5,
            bottomColor = 0xFFF0FDF4,
            accentGlow = 0xFF10B981,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_peach_sunset",
            nameEs = "Atardecer Durazno",
            nameEn = "Peach Sunset",
            nameJa = "ピーチサンセット",
            isDark = false,
            topColor = 0xFFFFEDD5,
            bottomColor = 0xFFFFF7ED,
            accentGlow = 0xFFFB923C,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_lavender_mist",
            nameEs = "Bruma Lavanda",
            nameEn = "Lavender Mist",
            nameJa = "ラベンダーミスト",
            isDark = false,
            topColor = 0xFFEDE9FE,
            bottomColor = 0xFFFAF5FF,
            accentGlow = 0xFFA855F7,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_oceanic_aqua",
            nameEs = "Aqua Océano",
            nameEn = "Oceanic Aqua",
            nameJa = "オーシャンアクア",
            isDark = false,
            topColor = 0xFFCFFAFE,
            bottomColor = 0xFFECFEFF,
            accentGlow = 0xFF06B6D4,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_golden_sunbeam",
            nameEs = "Rayo Dorado",
            nameEn = "Golden Sunbeam",
            nameJa = "ゴールデンサンビーム",
            isDark = false,
            topColor = 0xFFFEF3C7,
            bottomColor = 0xFFFFFBEB,
            accentGlow = 0xFFF59E0B,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_cotton_cloud",
            nameEs = "Nube de Algodón",
            nameEn = "Cotton Cloud",
            nameJa = "コットンクラウド",
            isDark = false,
            topColor = 0xFFE2E8F0,
            bottomColor = 0xFFF8FAFC,
            accentGlow = 0xFF64748B,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_matcha_dew",
            nameEs = "Rocío Matcha",
            nameEn = "Matcha Dew",
            nameJa = "抹茶のしずく",
            isDark = false,
            topColor = 0xFFDCFCE7,
            bottomColor = 0xFFF7FEE7,
            accentGlow = 0xFF84CC16,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_iridescent_lilac",
            nameEs = "Lila Iridiscente",
            nameEn = "Iridescent Lilac",
            nameJa = "虹色ライラック",
            isDark = false,
            topColor = 0xFFE0E7FF,
            bottomColor = 0xFFFDF4FF,
            accentGlow = 0xFF6366F1,
            isLightText = false
        ),
        WallpaperTheme(
            id = "light_sand_dune",
            nameEs = "Duna de Nieve",
            nameEn = "Snow Dune",
            nameJa = "雪の砂丘",
            isDark = false,
            topColor = 0xFFF3E8E2,
            bottomColor = 0xFFFAF7F5,
            accentGlow = 0xFFC29B7F,
            isLightText = false
        )
    )

    val darkWallpapers = listOf(
        WallpaperTheme(
            id = "dark_midnight_oled",
            nameEs = "OLED Medianoche",
            nameEn = "Midnight OLED",
            nameJa = "ミッドナイトOLED",
            isDark = true,
            topColor = 0xFF080D1A,
            bottomColor = 0xFF020408,
            accentGlow = 0xFF38BDF8,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_deep_cosmos",
            nameEs = "Cosmos Profundo",
            nameEn = "Deep Cosmos",
            nameJa = "ディープコスモス",
            isDark = true,
            topColor = 0xFF0B102F,
            bottomColor = 0xFF030712,
            accentGlow = 0xFF818CF8,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_cyber_neon",
            nameEs = "Cyber Neón Púrpura",
            nameEn = "Cyber Neon Purple",
            nameJa = "サイバーネオンパープル",
            isDark = true,
            topColor = 0xFF240A34,
            bottomColor = 0xFF0B0211,
            accentGlow = 0xFFE879F9,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_obsidian_slate",
            nameEs = "Pizarra Obsidiana",
            nameEn = "Obsidian Slate",
            nameJa = "黒曜石スレート",
            isDark = true,
            topColor = 0xFF181F2C,
            bottomColor = 0xFF0B0F17,
            accentGlow = 0xFF94A3B8,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_emerald_abyss",
            nameEs = "Abismo Esmeralda",
            nameEn = "Emerald Abyss",
            nameJa = "エメラルドの深淵",
            isDark = true,
            topColor = 0xFF07241E,
            bottomColor = 0xFF02120F,
            accentGlow = 0xFF34D399,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_amethyst_night",
            nameEs = "Amatista Nocturna",
            nameEn = "Amethyst Night",
            nameJa = "アメジストナイト",
            isDark = true,
            topColor = 0xFF1C0D30,
            bottomColor = 0xFF080210,
            accentGlow = 0xFFC084FC,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_arctic_twilight",
            nameEs = "Crepúsculo Ártico",
            nameEn = "Arctic Twilight",
            nameJa = "北極の黄昏",
            isDark = true,
            topColor = 0xFF0B2138,
            bottomColor = 0xFF030E1A,
            accentGlow = 0xFF38BDF8,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_solar_flare",
            nameEs = "Destello Solar",
            nameEn = "Solar Flare",
            nameJa = "ソーラーフレア",
            isDark = true,
            topColor = 0xFF2B1C0B,
            bottomColor = 0xFF0F0A03,
            accentGlow = 0xFFFBBF24,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_deep_trench",
            nameEs = "Fosa Abisal",
            nameEn = "Deep Trench",
            nameJa = "深海溝",
            isDark = true,
            topColor = 0xFF05222E,
            bottomColor = 0xFF020E14,
            accentGlow = 0xFF2DD4BF,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_crimson_smoky",
            nameEs = "Carmesí Humeante",
            nameEn = "Smoky Crimson",
            nameJa = "スモーキークリムゾン",
            isDark = true,
            topColor = 0xFF260B12,
            bottomColor = 0xFF0D0206,
            accentGlow = 0xFFFB7185,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_aurora_teal",
            nameEs = "Aurora Boreal Verde",
            nameEn = "Aurora Borealis Green",
            nameJa = "オーロラグリーンダーク",
            isDark = true,
            topColor = 0xFF072626,
            bottomColor = 0xFF021313,
            accentGlow = 0xFF4ADE80,
            isLightText = true
        ),
        WallpaperTheme(
            id = "dark_titan_matte",
            nameEs = "Titán Mate",
            nameEn = "Matte Titanium",
            nameJa = "マットチタン",
            isDark = true,
            topColor = 0xFF1C1C21,
            bottomColor = 0xFF0C0C0E,
            accentGlow = 0xFFA1A1AA,
            isLightText = true
        )
    )

    fun findWallpaper(id: String): WallpaperTheme {
        return lightWallpapers.find { it.id == id }
            ?: darkWallpapers.find { it.id == id }
            ?: lightWallpapers.first()
    }
}
