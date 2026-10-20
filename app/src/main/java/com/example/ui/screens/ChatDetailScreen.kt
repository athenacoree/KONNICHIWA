package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppLanguage
import com.example.model.Friend
import com.example.model.InteractiveCardData
import com.example.model.Message
import com.example.model.MessageType
import com.example.ui.components.Card3DInteractive
import com.example.ui.components.CardEditorModal
import com.example.ui.components.VoiceRecordingOrbOverlay
import com.example.ui.components.YouTubeEmbedPlayer
import com.example.util.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    friend: Friend,
    messages: List<Message>,
    onBack: () -> Unit,
    onSendMessage: (text: String, mediaUrl: String?) -> Unit,
    onSendCard: (InteractiveCardData) -> Unit,
    onVoiceSend: (durationSeconds: Int) -> Unit,
    onOpenThread: (Message) -> Unit,
    onToggleReaction: (messageId: String, emoji: String) -> Unit,
    onVoteOption: (messageId: String, optionId: String) -> Unit,
    onToggleCardAction: (messageId: String) -> Unit,
    userName: String,
    isDark: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var textInput by remember { mutableStateOf("") }
    var isRecordingAudio by remember { mutableStateOf(false) }
    var activeReactionMessageId by remember { mutableStateOf<String?>(null) }
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var showCardEditor by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = textColor
                    )
                }

                // Friend Avatar
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(
                            1.5.dp,
                            if (friend.isOnline) Color(0xFF22C55E) else Color.White.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    AsyncImage(
                        model = friend.avatarUrl,
                        contentDescription = friend.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = friend.name,
                        color = textColor,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (friend.isOnline) Strings.online(language) else friend.lastSeenText,
                        color = if (friend.isOnline) Color(0xFF16A34A) else subTextColor,
                        fontSize = 12.sp
                    )
                }

                // Video / YouTube sample link button
                IconButton(
                    onClick = { textInput = "https://www.youtube.com/watch?v=jfKfPfyJRdk" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "YouTube Link",
                        tint = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                    )
                }
            }

            // Messages LazyColumn
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("chat_messages_list"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    val isMe = msg.senderId == "me"

                    ChatMessageBubble(
                        message = msg,
                        isMe = isMe,
                        isDark = isDark,
                        language = language,
                        onOpenThread = { onOpenThread(msg) },
                        onVoteOption = { optId -> onVoteOption(msg.id, optId) },
                        onToggleCardAction = { onToggleCardAction(msg.id) },
                        onLongPress = {
                            activeReactionMessageId = if (activeReactionMessageId == msg.id) null else msg.id
                        },
                        onReact = { emoji ->
                            onToggleReaction(msg.id, emoji)
                            activeReactionMessageId = null
                        },
                        showReactionPicker = activeReactionMessageId == msg.id
                    )
                }
            }

            // Input Bar with the Cross (+) Button as requested
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cross (+) Button that opens card creator & attachment options
                IconButton(
                    onClick = { showAttachmentMenu = true },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (isDark) listOf(Color(0xFF38BDF8), Color(0xFF818CF8))
                                else listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                            )
                        )
                        .testTag("chat_plus_action_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Opciones",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Text field
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text(Strings.writeMessage(language), fontSize = 14.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_message_input"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.12f),
                        focusedContainerColor = if (isDark) Color(0xFF0F172A).copy(alpha = 0.7f) else Color.White.copy(alpha = 0.85f),
                        unfocusedContainerColor = if (isDark) Color(0xFF0F172A).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (textInput.isNotBlank()) {
                    // Send text button
                    IconButton(
                        onClick = {
                            onSendMessage(textInput, null)
                            textInput = ""
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF38BDF8), Color(0xFF818CF8))
                                )
                            )
                            .testTag("send_message_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    // Mic button (Triggers Voice Orb docked at bottom!)
                    IconButton(
                        onClick = { isRecordingAudio = true },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                                )
                            )
                            .testTag("mic_record_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Record Audio",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // Voice Orb Overlay (Docked to bottom with deep frosted blur)
        AnimatedVisibility(
            visible = isRecordingAudio,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            VoiceRecordingOrbOverlay(
                isRecording = isRecordingAudio,
                onCancel = { isRecordingAudio = false },
                onSend = { durationSeconds ->
                    isRecordingAudio = false
                    onVoiceSend(durationSeconds)
                },
                isDark = isDark
            )
        }

        // Attachment Options Bottom Sheet (opened by the Cross + button)
        if (showAttachmentMenu) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = { showAttachmentMenu = false },
                sheetState = sheetState,
                containerColor = if (isDark) Color(0xFF0B132B) else Color(0xFFF8FAFC),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Compartir en el chat",
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. Crear Tarjeta 3D Libre
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isDark) Color(0xFF1E293B) else Color.White)
                            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                            .clickable {
                                showAttachmentMenu = false
                                showCardEditor = true
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF818CF8)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Style, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Crear Tarjeta 3D Interactiva",
                                color = textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Invitaciones, saludos, encuestas en vivo y mini-juegos",
                                color = subTextColor,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. Subir Foto
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isDark) Color(0xFF1E293B) else Color.White)
                            .clickable {
                                onSendMessage(
                                    "Foto compartida",
                                    "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&auto=format&fit=crop&q=80"
                                )
                                showAttachmentMenu = false
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Subir Foto", color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Compartir fotos en alta resolución", color = subTextColor, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3. Enviar Archivo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isDark) Color(0xFF1E293B) else Color.White)
                            .clickable {
                                onSendMessage("📄 Documento_Konnichiwa_Audio.zip (2.4 MB)", null)
                                showAttachmentMenu = false
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFA855F7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Enviar Archivo", color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Documentos, audio sin compresión, etc.", color = subTextColor, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Card Editor Modal
        if (showCardEditor) {
            CardEditorModal(
                onDismiss = { showCardEditor = false },
                onSendCard = { card ->
                    onSendCard(card)
                },
                senderName = userName,
                isDark = isDark
            )
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: Message,
    isMe: Boolean,
    isDark: Boolean,
    language: AppLanguage,
    onOpenThread: () -> Unit,
    onVoteOption: (String) -> Unit,
    onToggleCardAction: () -> Unit,
    onLongPress: () -> Unit,
    onReact: (String) -> Unit,
    showReactionPicker: Boolean
) {
    val bubbleShape = if (isMe) {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
    }

    val bubbleBackground = if (isMe) {
        Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF38BDF8)))
    } else {
        if (isDark) {
            Brush.linearGradient(listOf(Color(0xFF1E293B).copy(alpha = 0.9f), Color(0xFF0F172A).copy(alpha = 0.95f)))
        } else {
            Brush.linearGradient(listOf(Color.White.copy(alpha = 0.95f), Color(0xFFF1F5F9).copy(alpha = 0.95f)))
        }
    }

    val bubbleTextColor = if (isMe) Color.White else (if (isDark) Color.White else Color(0xFF0F172A))
    val bubbleSubTextColor = if (isMe) Color.White.copy(alpha = 0.8f) else (if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        // Quick reaction bar picker
        if (showReactionPicker) {
            Row(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDark) Color(0xFF1E293B) else Color.White)
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("❤️", "🔥", "😂", "😮", "👏", "✨").forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable { onReact(emoji) }
                            .padding(4.dp)
                    )
                }
            }
        }

        // If it's an Interactive 3D Card
        if (message.type == MessageType.CARD && message.cardData != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { onLongPress() },
                            onDoubleTap = { onReact("❤️") }
                        )
                    }
            ) {
                Card3DInteractive(
                    card = message.cardData,
                    onVoteOption = onVoteOption,
                    onToggleAction = onToggleCardAction,
                    isDark = isDark
                )
            }
        } else {
            // Standard Text / Image / Audio / YouTube Bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (message.type == MessageType.YOUTUBE || message.type == MessageType.IMAGE) 0.88f else 0.80f)
                    .shadow(4.dp, bubbleShape)
                    .clip(bubbleShape)
                    .background(bubbleBackground)
                    .border(
                        width = 1.dp,
                        color = if (isMe) Color.White.copy(alpha = 0.3f) else (if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.8f)),
                        shape = bubbleShape
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { onLongPress() },
                            onDoubleTap = { onReact("❤️") }
                        )
                    }
                    .padding(if (message.type == MessageType.IMAGE || message.type == MessageType.YOUTUBE) 8.dp else 14.dp)
            ) {
                Column {
                    when (message.type) {
                        MessageType.TEXT -> {
                            Text(
                                text = message.text,
                                color = bubbleTextColor,
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            )
                        }

                        MessageType.IMAGE -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                AsyncImage(
                                    model = message.mediaUrl,
                                    contentDescription = "Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            if (message.text.isNotBlank() && message.text != "Foto compartida") {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = message.text,
                                    color = bubbleTextColor,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        MessageType.AUDIO -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isMe) Color.White.copy(alpha = 0.3f) else Color(0xFF38BDF8)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Nota de voz • ${message.audioDurationSeconds}s",
                                        color = bubbleTextColor,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val bars = if (message.audioWaveform.isNotEmpty()) message.audioWaveform else listOf(0.4f, 0.7f, 0.3f, 0.9f, 0.5f, 0.8f)
                                        bars.forEach { h ->
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height((h * 18).coerceAtLeast(3f).dp)
                                                    .background(if (isMe) Color.White else Color(0xFF38BDF8), RoundedCornerShape(1.dp))
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        MessageType.YOUTUBE -> {
                            Column {
                                if (message.youtubeVideoId != null) {
                                    YouTubeEmbedPlayer(
                                        videoId = message.youtubeVideoId,
                                        isDark = isDark
                                    )
                                } else {
                                    Text(
                                        text = message.text,
                                        color = bubbleTextColor,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        MessageType.CARD -> { }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Timestamp & Double check status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message.formattedTime,
                            color = bubbleSubTextColor,
                            fontSize = 11.sp
                        )
                        if (isMe) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = "Read",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Thread indicator
                    if (message.threadReplies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isMe) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.08f))
                                .clickable(onClick = onOpenThread)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = "Thread",
                                tint = bubbleTextColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = Strings.threadReplies(message.threadReplies.size, language),
                                color = bubbleTextColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Active Reactions Pill
        if (message.reactions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, start = if (isMe) 0.dp else 8.dp, end = if (isMe) 8.dp else 0.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) Color(0xFF1E293B) else Color.White)
                    .border(0.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                message.reactions.forEach { (emoji, count) ->
                    Text(
                        text = "$emoji $count",
                        fontSize = 12.sp,
                        color = if (isDark) Color.White else Color(0xFF0F172A)
                    )
                }
            }
        }
    }
}
