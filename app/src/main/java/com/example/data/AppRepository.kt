package com.example.data

import com.example.model.AppLanguage
import com.example.model.Friend
import com.example.model.FullAppSettings
import com.example.model.InteractionEvent
import com.example.model.InteractiveCardData
import com.example.model.Message
import com.example.model.MessageType
import com.example.model.ThreadReply
import com.example.model.UserProfile
import com.example.util.YouTubeHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AppRepository {

    private val _friends = MutableStateFlow(SampleData.friends)
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()

    private val _messagesByFriend = MutableStateFlow<Map<String, List<Message>>>(
        SampleData.friends.associate { friend ->
            friend.id to SampleData.initialMessages(friend.id)
        }
    )
    val messagesByFriend: StateFlow<Map<String, List<Message>>> = _messagesByFriend.asStateFlow()

    private val _interactions = MutableStateFlow(SampleData.initialInteractions)
    val interactions: StateFlow<List<InteractionEvent>> = _interactions.asStateFlow()

    private val _currentUserProfile = MutableStateFlow(UserProfile())
    val currentUserProfile: StateFlow<UserProfile> = _currentUserProfile.asStateFlow()

    private val _settings = MutableStateFlow(FullAppSettings())
    val settings: StateFlow<FullAppSettings> = _settings.asStateFlow()

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun sendMessage(
        friendId: String,
        text: String,
        mediaUrl: String? = null,
        audioDurationSeconds: Int = 0,
        cardData: InteractiveCardData? = null
    ) {
        val currentTime = timeFormat.format(Date())
        val youtubeId = YouTubeHelper.extractVideoId(text)

        val messageType = when {
            cardData != null -> MessageType.CARD
            youtubeId != null -> MessageType.YOUTUBE
            audioDurationSeconds > 0 -> MessageType.AUDIO
            !mediaUrl.isNullOrEmpty() -> MessageType.IMAGE
            else -> MessageType.TEXT
        }

        // Generate realistic dynamic audio waveform bars matching audio length
        val waveform = if (audioDurationSeconds > 0) {
            val barsCount = (audioDurationSeconds * 2).coerceIn(10, 24)
            (0 until barsCount).map { i ->
                val wave = (kotlin.math.sin(i * 0.7f) * 0.4f + 0.55f).coerceIn(0.2f, 0.95f)
                (wave * 100).toInt() / 100f
            }
        } else emptyList()

        val newMessage = Message(
            id = "msg_${UUID.randomUUID().toString().take(8)}",
            senderId = "me",
            recipientId = friendId,
            text = text,
            type = messageType,
            mediaUrl = mediaUrl,
            audioDurationSeconds = audioDurationSeconds,
            audioWaveform = waveform,
            youtubeVideoId = youtubeId,
            cardData = cardData,
            formattedTime = currentTime,
            isRead = true
        )

        val currentList = _messagesByFriend.value[friendId].orEmpty()
        _messagesByFriend.value = _messagesByFriend.value + (friendId to (currentList + newMessage))

        // Also broadcast as a new interaction
        val friend = _friends.value.find { it.id == friendId } ?: return
        val newEvent = InteractionEvent(
            id = "ie_${UUID.randomUUID().toString().take(8)}",
            friend = friend,
            message = newMessage,
            timeAgo = "ahora mismo",
            isUnread = false
        )
        _interactions.value = listOf(newEvent) + _interactions.value
    }

    fun voteOnPoll(friendId: String, messageId: String, optionId: String) {
        val friendMsgs = _messagesByFriend.value[friendId].orEmpty()
        val updated = friendMsgs.map { msg ->
            if (msg.id == messageId && msg.cardData != null) {
                val currentCard = msg.cardData
                if (currentCard.userVotedOptionId != null) return@map msg // already voted
                val updatedOptions = currentCard.pollOptions.map { opt ->
                    if (opt.id == optionId) opt.copy(votes = opt.votes + 1) else opt
                }
                msg.copy(
                    cardData = currentCard.copy(
                        pollOptions = updatedOptions,
                        userVotedOptionId = optionId
                    )
                )
            } else msg
        }
        _messagesByFriend.value = _messagesByFriend.value + (friendId to updated)
    }

    fun toggleCardAction(friendId: String, messageId: String) {
        val friendMsgs = _messagesByFriend.value[friendId].orEmpty()
        val updated = friendMsgs.map { msg ->
            if (msg.id == messageId && msg.cardData != null) {
                val current = msg.cardData
                msg.copy(cardData = current.copy(isActionCompleted = !current.isActionCompleted))
            } else msg
        }
        _messagesByFriend.value = _messagesByFriend.value + (friendId to updated)
    }

    fun addIncomingMessageFromFriend(
        friendId: String,
        text: String,
        mediaUrl: String? = null,
        isAudio: Boolean = false,
        cardData: InteractiveCardData? = null
    ) {
        val currentTime = timeFormat.format(Date())
        val youtubeId = YouTubeHelper.extractVideoId(text)
        val friend = _friends.value.find { it.id == friendId } ?: return

        val messageType = when {
            cardData != null -> MessageType.CARD
            youtubeId != null -> MessageType.YOUTUBE
            isAudio -> MessageType.AUDIO
            !mediaUrl.isNullOrEmpty() -> MessageType.IMAGE
            else -> MessageType.TEXT
        }

        val incomingMessage = Message(
            id = "msg_${UUID.randomUUID().toString().take(8)}",
            senderId = friendId,
            recipientId = "me",
            text = text,
            type = messageType,
            mediaUrl = mediaUrl,
            audioDurationSeconds = if (isAudio) 8 else 0,
            audioWaveform = if (isAudio) listOf(0.4f, 0.8f, 0.5f, 0.9f, 0.6f, 0.7f, 0.3f, 0.8f, 0.5f, 0.7f) else emptyList(),
            youtubeVideoId = youtubeId,
            cardData = cardData,
            formattedTime = currentTime,
            isRead = false
        )

        val currentList = _messagesByFriend.value[friendId].orEmpty()
        _messagesByFriend.value = _messagesByFriend.value + (friendId to (currentList + incomingMessage))

        val newEvent = InteractionEvent(
            id = "ie_${UUID.randomUUID().toString().take(8)}",
            friend = friend,
            message = incomingMessage,
            timeAgo = "ahora mismo",
            isUnread = true
        )
        _interactions.value = listOf(newEvent) + _interactions.value
    }

    fun toggleReaction(friendId: String, messageId: String, emoji: String) {
        val friendMsgs = _messagesByFriend.value[friendId].orEmpty()
        val updated = friendMsgs.map { msg ->
            if (msg.id == messageId) {
                val currentCount = msg.reactions[emoji] ?: 0
                val newReactions = msg.reactions.toMutableMap()
                if (currentCount > 0) {
                    newReactions[emoji] = currentCount + 1
                } else {
                    newReactions[emoji] = 1
                }
                msg.copy(reactions = newReactions)
            } else msg
        }
        _messagesByFriend.value = _messagesByFriend.value + (friendId to updated)
    }

    fun addThreadReply(friendId: String, messageId: String, replyText: String) {
        val currentTime = timeFormat.format(Date())
        val user = _currentUserProfile.value

        val newReply = ThreadReply(
            id = "tr_${UUID.randomUUID().toString().take(8)}",
            senderName = user.name,
            senderAvatar = user.avatarUrl,
            text = replyText,
            time = currentTime,
            isMe = true
        )

        val friendMsgs = _messagesByFriend.value[friendId].orEmpty()
        val updated = friendMsgs.map { msg ->
            if (msg.id == messageId) {
                msg.copy(threadReplies = msg.threadReplies + newReply)
            } else msg
        }
        _messagesByFriend.value = _messagesByFriend.value + (friendId to updated)
    }

    fun addFriend(name: String, username: String, bio: String, avatarUrl: String) {
        val newFriend = Friend(
            id = "friend_${UUID.randomUUID().toString().take(8)}",
            name = name,
            username = username.removePrefix("@"),
            avatarUrl = avatarUrl.ifEmpty { "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=500&auto=format&fit=crop&q=80" },
            statusText = "¡Nuevo amigo en Konnichiwa! 🪐",
            isOnline = true,
            lastSeenText = "En línea",
            bio = bio.ifEmpty { "Compartiendo momentos privados en Konnichiwa" },
            vibeScore = 100,
            sharedMemoriesCount = 1
        )
        _friends.value = listOf(newFriend) + _friends.value
        _messagesByFriend.value = _messagesByFriend.value + (newFriend.id to listOf(
            Message(
                id = "welcome_${newFriend.id}",
                senderId = newFriend.id,
                recipientId = "me",
                text = "¡Konnichiwa! Ya estamos conectados 🎉",
                formattedTime = timeFormat.format(Date())
            )
        ))
    }

    fun updateProfile(name: String, username: String, bio: String, statusMood: String, avatarUrl: String) {
        _currentUserProfile.value = _currentUserProfile.value.copy(
            name = name,
            username = username.removePrefix("@"),
            bio = bio,
            statusMood = statusMood,
            avatarUrl = if (avatarUrl.isNotEmpty()) avatarUrl else _currentUserProfile.value.avatarUrl
        )
    }

    fun updateSettings(newSettings: FullAppSettings) {
        _settings.value = newSettings
    }

    fun setWallpaper(wallpaperId: String, isDark: Boolean) {
        val currentStyle = _settings.value.style
        _settings.value = _settings.value.copy(
            style = currentStyle.copy(
                selectedWallpaperId = wallpaperId,
                isDarkMode = isDark
            )
        )
    }

    fun setLanguage(language: AppLanguage) {
        _settings.value = _settings.value.copy(language = language)
    }
}
