package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.FullAppSettings
import com.example.model.WallpaperCatalog
import com.example.model.WallpaperTheme
import com.example.util.Strings

@Composable
fun SettingsScreen(
    settings: FullAppSettings,
    onUpdateSettings: (FullAppSettings) -> Unit,
    onSelectWallpaper: (id: String, isDark: Boolean) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    // Expandable folders state (one below the other, WhatsApp style)
    val expandedFolders = remember {
        mutableStateMapOf(
            "style" to true,
            "privacy" to false,
            "security" to false,
            "accessibility" to false
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Settings Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Strings.settings(settings.language),
                color = textColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            // Language Selector Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDark) Color(0xFF1E293B) else Color.White)
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppLanguage.values().forEach { lang ->
                    val isSelected = settings.language == lang
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) (if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)) else Color.Transparent)
                            .clickable { onSelectLanguage(lang) }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang.displayName.take(3),
                            color = if (isSelected) Color.White else subTextColor,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // WhatsApp Style Folders Stacked Vertically
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .testTag("settings_list"),
            contentPadding = PaddingValues(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // FOLDER 1: ESTILO & FONDOS (12 Light + 12 Dark Wallpapers + Style Controls)
            item {
                WhatsAppSettingsFolder(
                    title = Strings.style(settings.language),
                    subtitle = "12 Fondos Claros, 12 Oscuros, Modo Oscuro, 3D",
                    icon = Icons.Default.ColorLens,
                    isExpanded = expandedFolders["style"] == true,
                    onToggle = { expandedFolders["style"] = !(expandedFolders["style"] ?: false) },
                    isDark = isDark,
                    textColor = textColor,
                    subTextColor = subTextColor
                ) {
                    // Dark Mode toggle
                    SettingSwitchRow("Modo Oscuro", settings.style.isDarkMode, textColor, subTextColor) { isDarkSelected ->
                        val defaultWall = if (isDarkSelected) "dark_midnight_oled" else "light_aurora_sky"
                        onSelectWallpaper(defaultWall, isDarkSelected)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 12 Light Wallpapers
                    Text("12 Fondos Claros Cambiables", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Predeterminado: Azul clarito y medio blanco", color = subTextColor, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        items(WallpaperCatalog.lightWallpapers) { wall ->
                            WallpaperThumbnailCard(
                                wallpaper = wall,
                                isSelected = settings.style.selectedWallpaperId == wall.id,
                                onClick = { onSelectWallpaper(wall.id, false) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 12 Dark Wallpapers
                    Text("12 Fondos Oscuros Cambiables", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        items(WallpaperCatalog.darkWallpapers) { wall ->
                            WallpaperThumbnailCard(
                                wallpaper = wall,
                                isSelected = settings.style.selectedWallpaperId == wall.id,
                                onClick = { onSelectWallpaper(wall.id, true) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Intensidad de desenfoque de cristal: ${(settings.style.glassBlurIntensity * 100).toInt()}%", color = subTextColor, fontSize = 13.sp)
                    Slider(
                        value = settings.style.glassBlurIntensity,
                        onValueChange = { onUpdateSettings(settings.copy(style = settings.style.copy(glassBlurIntensity = it))) },
                        valueRange = 0.4f..1f,
                        colors = SliderDefaults.colors(thumbColor = Color(0xFF38BDF8), activeTrackColor = Color(0xFF38BDF8))
                    )

                    SettingSwitchRow("Bordes con reflejo iridiscente", settings.style.iridescentBorderGlow, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(style = settings.style.copy(iridescentBorderGlow = it)))
                    }
                    SettingSwitchRow("Efecto 3D de inclinación en tarjetas", settings.style.card3DTiltEffect, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(style = settings.style.copy(card3DTiltEffect = it)))
                    }
                    SettingSelectorRow("Estilo de burbujas", settings.style.bubbleStyle, textColor, subTextColor) {
                        val next = if (settings.style.bubbleStyle == "Telegram-Instagram") "Pure Glass" else "Telegram-Instagram"
                        onUpdateSettings(settings.copy(style = settings.style.copy(bubbleStyle = next)))
                    }
                    SettingSelectorRow("Radio de esquinas", "${settings.style.cardCornerRadiusDp}dp", textColor, subTextColor) {
                        val next = if (settings.style.cardCornerRadiusDp == 24) 28 else 24
                        onUpdateSettings(settings.copy(style = settings.style.copy(cardCornerRadiusDp = next)))
                    }
                    SettingSelectorRow("Color de acento", settings.style.accentColorTheme, textColor, subTextColor) {
                        val next = if (settings.style.accentColorTheme == "Neon Sky") "Sakura Pink" else "Neon Sky"
                        onUpdateSettings(settings.copy(style = settings.style.copy(accentColorTheme = next)))
                    }
                    SettingSelectorRow("Estilo de esfera de voz", settings.style.voiceOrbHologramStyle, textColor, subTextColor) {
                        val next = if (settings.style.voiceOrbHologramStyle == "ChatGPT Fluid") "Siri Wave" else "ChatGPT Fluid"
                        onUpdateSettings(settings.copy(style = settings.style.copy(voiceOrbHologramStyle = next)))
                    }
                    SettingSwitchRow("Aura de escritura de amigos", settings.style.showTypingAura, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(style = settings.style.copy(showTypingAura = it)))
                    }
                }
            }

            // FOLDER 2: PRIVACIDAD (11 Settings)
            item {
                WhatsAppSettingsFolder(
                    title = Strings.privacy(settings.language),
                    subtitle = "Última vez, doble visto, capturas, auto-borrado",
                    icon = Icons.Default.Visibility,
                    isExpanded = expandedFolders["privacy"] == true,
                    onToggle = { expandedFolders["privacy"] = !(expandedFolders["privacy"] ?: false) },
                    isDark = isDark,
                    textColor = textColor,
                    subTextColor = subTextColor
                ) {
                    SettingSelectorRow("Última vez visto", settings.privacy.lastSeenVisibility, textColor, subTextColor) {
                        val next = when (settings.privacy.lastSeenVisibility) {
                            "Amigos" -> "Nadie"
                            "Nadie" -> "Todos"
                            else -> "Amigos"
                        }
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(lastSeenVisibility = next)))
                    }
                    SettingSwitchRow("Confirmaciones de lectura (Doble visto)", settings.privacy.readReceipts, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(readReceipts = it)))
                    }
                    SettingSwitchRow("Ocultar estado en línea", settings.privacy.hideOnlineStatus, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(hideOnlineStatus = it)))
                    }
                    SettingSwitchRow("Fotos y estados efímeros", settings.privacy.ephemeralMedia, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(ephemeralMedia = it)))
                    }
                    SettingSelectorRow("Visibilidad de hilos", settings.privacy.threadsVisibility, textColor, subTextColor) {
                        val next = if (settings.privacy.threadsVisibility == "Solo Amigos") "Privado" else "Solo Amigos"
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(threadsVisibility = next)))
                    }
                    SettingSwitchRow("Alertas de captura de pantalla", settings.privacy.screenshotAlerts, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(screenshotAlerts = it)))
                    }
                    SettingSwitchRow("Escucha incógnita de notas de voz", settings.privacy.incognitoVoiceListening, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(incognitoVoiceListening = it)))
                    }
                    SettingSelectorRow("Auto-borrado de chats antiguos", if (settings.privacy.autoDeleteChatDays == 0) "Desactivado" else "${settings.privacy.autoDeleteChatDays} días", textColor, subTextColor) {
                        val next = if (settings.privacy.autoDeleteChatDays == 0) 7 else 0
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(autoDeleteChatDays = next)))
                    }
                    SettingSwitchRow("Ocultar vista previa en notificaciones", settings.privacy.hideNotificationPreview, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(hideNotificationPreview = it)))
                    }
                    SettingSwitchRow("Ocultar videos que estás viendo", settings.privacy.hideYouTubeWatchStatus, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(hideYouTubeWatchStatus = it)))
                    }
                    SettingSwitchRow("Sincronización de Vibe con amigos", settings.privacy.allowFriendVibeSync, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(privacy = settings.privacy.copy(allowFriendVibeSync = it)))
                    }
                }
            }

            // FOLDER 3: SEGURIDAD (11 Settings)
            item {
                WhatsAppSettingsFolder(
                    title = Strings.security(settings.language),
                    subtitle = "Biometría, PIN, E2EE, Anti-phishing, 2FA",
                    icon = Icons.Default.Shield,
                    isExpanded = expandedFolders["security"] == true,
                    onToggle = { expandedFolders["security"] = !(expandedFolders["security"] ?: false) },
                    isDark = isDark,
                    textColor = textColor,
                    subTextColor = subTextColor
                ) {
                    SettingSwitchRow("Bloqueo biométrico (Huella / Rostro)", settings.security.biometricLockEnabled, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(biometricLockEnabled = it)))
                    }
                    SettingSwitchRow("Código PIN de acceso de 4 dígitos", settings.security.pinProtectionEnabled, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(pinProtectionEnabled = it)))
                    }
                    SettingSwitchRow("Cifrado de extremo a extremo activo", settings.security.endToEndEncryptionActive, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(endToEndEncryptionActive = it)))
                    }
                    SettingSwitchRow("Escudo anti-phishing", settings.security.antiPhishingShield, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(antiPhishingShield = it)))
                    }
                    SettingSwitchRow("Alertas de inicios de sesión sospechosos", settings.security.suspiciousLoginAlerts, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(suspiciousLoginAlerts = it)))
                    }
                    SettingSwitchRow("Autenticación en dos pasos (2FA)", settings.security.twoFactorAuth, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(twoFactorAuth = it)))
                    }
                    SettingSwitchRow("Auto-destrucción de mensajes al salir", settings.security.selfDestructOnExit, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(selfDestructOnExit = it)))
                    }
                    SettingSwitchRow("Sandbox seguro para enlaces externos", settings.security.safeLinkSandbox, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(safeLinkSandbox = it)))
                    }
                    SettingSwitchRow("Atajo de pánico para borrado rápido", settings.security.panicEmergencyWipeShortcut, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(panicEmergencyWipeShortcut = it)))
                    }
                    SettingSwitchRow("Copia de seguridad encriptada", settings.security.encryptedCloudBackups, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(security = settings.security.copy(encryptedCloudBackups = it)))
                    }
                    SettingSelectorRow("Dispositivos activos", "1 teléfono activo", textColor, subTextColor) { }
                }
            }

            // FOLDER 4: ACCESIBILIDAD (11 Settings)
            item {
                WhatsAppSettingsFolder(
                    title = Strings.accessibility(settings.language),
                    subtitle = "Texto dinámico, contraste, hápticos, subtítulos",
                    icon = Icons.Default.Accessibility,
                    isExpanded = expandedFolders["accessibility"] == true,
                    onToggle = { expandedFolders["accessibility"] = !(expandedFolders["accessibility"] ?: false) },
                    isDark = isDark,
                    textColor = textColor,
                    subTextColor = subTextColor
                ) {
                    Text("Tamaño de texto dinámico: ${(settings.accessibility.dynamicTextScale * 100).toInt()}%", color = subTextColor, fontSize = 13.sp)
                    Slider(
                        value = settings.accessibility.dynamicTextScale,
                        onValueChange = { onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(dynamicTextScale = it))) },
                        valueRange = 0.85f..1.35f,
                        colors = SliderDefaults.colors(thumbColor = Color(0xFF38BDF8), activeTrackColor = Color(0xFF38BDF8))
                    )
                    SettingSwitchRow("Texto de alto contraste", settings.accessibility.highContrastText, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(highContrastText = it)))
                    }
                    SettingSwitchRow("Reducción de movimiento y animaciones", settings.accessibility.reduceMotion, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(reduceMotion = it)))
                    }
                    SettingSwitchRow("Vibración y respuesta háptica", settings.accessibility.hapticFeedback, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(hapticFeedback = it)))
                    }
                    SettingSwitchRow("Efectos de sonido en el chat", settings.accessibility.chatSoundEffects, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(chatSoundEffects = it)))
                    }
                    SettingSwitchRow("Transcripción automática de notas de voz", settings.accessibility.autoVoiceTranscription, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(autoVoiceTranscription = it)))
                    }
                    SettingSwitchRow("Subtítulos automáticos en videos", settings.accessibility.autoVideoSubtitles, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(autoVideoSubtitles = it)))
                    }
                    SettingSwitchRow("Lector de pantalla optimizado", settings.accessibility.screenReaderOptimized, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(screenReaderOptimized = it)))
                    }
                    SettingSwitchRow("Botones de toque extendido (48dp+)", settings.accessibility.largeTouchTargets, textColor, subTextColor) {
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(largeTouchTargets = it)))
                    }
                    SettingSelectorRow("Tiempo de pulsación larga", "${settings.accessibility.longPressDurationMs}ms", textColor, subTextColor) {
                        val next = if (settings.accessibility.longPressDurationMs == 300) 500 else 300
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(longPressDurationMs = next)))
                    }
                    SettingSelectorRow("Filtro daltónico de color", settings.accessibility.colorBlindFilter, textColor, subTextColor) {
                        val next = if (settings.accessibility.colorBlindFilter == "Normal") "Protanopia" else "Normal"
                        onUpdateSettings(settings.copy(accessibility = settings.accessibility.copy(colorBlindFilter = next)))
                    }
                }
            }
        }
    }
}

@Composable
private fun WhatsAppSettingsFolder(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    isDark: Boolean,
    textColor: Color,
    subTextColor: Color,
    content: @Composable () -> Unit
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "arrow_rot"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E293B).copy(alpha = 0.88f) else Color.White.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.9f),
                RoundedCornerShape(22.dp)
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Folder Header Row (clickable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (isDark) listOf(Color(0xFF38BDF8), Color(0xFF818CF8))
                                else listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = subTextColor,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = subTextColor,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(arrowRotation)
                )
            }

            // Expandable Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun WallpaperThumbnailCard(
    wallpaper: WallpaperTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(82.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .shadow(6.dp, RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(wallpaper.topColor), Color(wallpaper.bottomColor))
                    )
                )
                .border(
                    width = if (isSelected) 2.5.dp else 1.dp,
                    color = if (isSelected) Color(0xFF38BDF8) else Color.White.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF38BDF8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = wallpaper.nameEs,
            fontSize = 10.sp,
            maxLines = 1,
            color = if (wallpaper.isDark) Color.White else Color(0xFF0F172A),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    checked: Boolean,
    textColor: Color,
    subTextColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF38BDF8)
            )
        )
    }
}

@Composable
private fun SettingSelectorRow(
    title: String,
    value: String,
    textColor: Color,
    subTextColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = Color(0xFF38BDF8),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
