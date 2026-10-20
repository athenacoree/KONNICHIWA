package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CardType
import com.example.model.CardVisualEffect
import com.example.model.InteractiveCardData
import com.example.model.PollOption
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEditorModal(
    onDismiss: () -> Unit,
    onSendCard: (InteractiveCardData) -> Unit,
    senderName: String,
    isDark: Boolean
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedType by remember { mutableStateOf(CardType.INVITATION) }
    var selectedEffect by remember { mutableStateOf(CardVisualEffect.COSMIC_SINGULARITY) }
    var title by remember { mutableStateOf("Noche Cósmica en la Azotea") }
    var description by remember { mutableStateOf("Música lofi, proyecciones y charla entre amigos") }
    var details by remember { mutableStateOf("Viernes 20:00 • Tokyo Lounge") }
    var selectedSticker by remember { mutableStateOf("🪐") }

    var pollOption1 by remember { mutableStateOf("¡Por supuesto! ✨") }
    var pollOption2 by remember { mutableStateOf("Quizás más tarde ☕") }
    var pollOption3 by remember { mutableStateOf("Me apunto 100% 🔥") }

    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) Color(0xFF0B132B) else Color(0xFFF8FAFC),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier.imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Editor Libre de Tarjetas 3D",
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = subTextColor)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1. Selector de Tipo de Tarjeta
            Text("1. Tipo de tarjeta", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CardType.values()) { type ->
                    val isSelected = selectedType == type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) Color(0xFF38BDF8)
                                else (if (isDark) Color(0xFF1E293B) else Color.White)
                            )
                            .border(1.dp, if (isSelected) Color.White else Color.Black.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                            .clickable {
                                selectedType = type
                                when (type) {
                                    CardType.INVITATION -> {
                                        title = "Invitación VIP Especial"
                                        description = "Estás invitado a compartir una sesión privada"
                                        details = "Sábado 21:00 • Tokyo Sky"
                                        selectedSticker = "🪐"
                                    }
                                    CardType.POLL -> {
                                        title = "¿Cuál será el próximo plan?"
                                        description = "Vota por la mejor opción para el finde:"
                                        details = ""
                                        selectedSticker = "📊"
                                    }
                                    CardType.MINI_GAME -> {
                                        title = "Reto Entre Amigos: Verdad o Reto"
                                        description = "Confiesa tu anécdota más graciosa de este año"
                                        details = "Premio: Un café de especialidad ☕"
                                        selectedSticker = "🎮"
                                    }
                                    CardType.VIBE_GREETING -> {
                                        title = "Konnichiwa! Vibe Cósmico"
                                        description = "Deseándote la mejor energía hoy ✨"
                                        details = "De tu amigo de confianza"
                                        selectedSticker = "✨"
                                    }
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = type.label,
                            color = if (isSelected) Color.White else textColor,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Catálogo de Efectos Visuales
            Text("2. Catálogo de efectos y acabados", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CardVisualEffect.values()) { effect ->
                    val isSelected = selectedEffect == effect
                    val glow = Color(effect.glowColor)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isDark) Color(0xFF1E293B) else Color.White)
                            .border(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) glow else Color.White.copy(alpha = 0.15f),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedEffect = effect }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(glow)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = effect.displayName,
                            color = textColor,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Sticker Emoji Selector
            Text("3. Sticker cósmico", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("🪐", "✨", "🔥", "🚀", "💎", "☕", "🎮", "🌊", "🎧", "🌸", "🖤", "🌌")) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (selectedSticker == emoji) Color(0xFF38BDF8).copy(alpha = 0.3f) else Color.Transparent)
                            .border(1.dp, if (selectedSticker == emoji) Color(0xFF38BDF8) else Color.Transparent, CircleShape)
                            .clickable { selectedSticker = emoji },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 20.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Formulario de Textos
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título de la tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8))
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Mensaje o descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8))
            )

            if (selectedType == CardType.INVITATION || selectedType == CardType.MINI_GAME) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Fecha / Lugar / Instrucciones") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8))
                )
            }

            if (selectedType == CardType.POLL) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Opciones de la encuesta", color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = pollOption1,
                    onValueChange = { pollOption1 = it },
                    label = { Text("Opción 1") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = pollOption2,
                    onValueChange = { pollOption2 = it },
                    label = { Text("Opción 2") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = pollOption3,
                    onValueChange = { pollOption3 = it },
                    label = { Text("Opción 3 (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 5. Botón Enviar Tarjeta
            Button(
                onClick = {
                    val pollOpts = if (selectedType == CardType.POLL) {
                        listOfNotNull(
                            PollOption(id = "opt_1", text = pollOption1, votes = 1),
                            PollOption(id = "opt_2", text = pollOption2, votes = 0),
                            if (pollOption3.isNotBlank()) PollOption(id = "opt_3", text = pollOption3, votes = 0) else null
                        )
                    } else emptyList()

                    val card = InteractiveCardData(
                        id = "card_${UUID.randomUUID().toString().take(8)}",
                        type = selectedType,
                        effect = selectedEffect,
                        title = title,
                        description = description,
                        details = details,
                        stickerEmoji = selectedSticker,
                        pollOptions = pollOpts,
                        creatorName = senderName
                    )
                    onSendCard(card)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8))
            ) {
                Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Enviar Tarjeta 3D al Chat",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
