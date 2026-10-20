package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.model.AppLanguage
import com.example.model.Friend
import com.example.model.FullAppSettings
import com.example.model.Message
import com.example.model.UserProfile
import com.example.ui.components.AppNavTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    val repository: AppRepository = AppRepository()
) : ViewModel() {

    val friends = repository.friends
    val messagesByFriend = repository.messagesByFriend
    val interactions = repository.interactions
    val currentUserProfile = repository.currentUserProfile
    val settings = repository.settings

    private val _currentTab = MutableStateFlow(AppNavTab.INTERACCIONES)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    private val _activeChatFriend = MutableStateFlow<Friend?>(null)
    val activeChatFriend: StateFlow<Friend?> = _activeChatFriend.asStateFlow()

    private val _activeThreadMessage = MutableStateFlow<Message?>(null)
    val activeThreadMessage: StateFlow<Message?> = _activeThreadMessage.asStateFlow()

    private val _isRecordingVoice = MutableStateFlow(false)
    val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

    private val _showAddFriendDialog = MutableStateFlow(false)
    val showAddFriendDialog: StateFlow<Boolean> = _showAddFriendDialog.asStateFlow()

    private val _showEditProfileSheet = MutableStateFlow(false)
    val showEditProfileSheet: StateFlow<Boolean> = _showEditProfileSheet.asStateFlow()

    init {
        // Periodic subtle simulation of friend activity (every 45 seconds or can be manually triggered)
        viewModelScope.launch {
            delay(15000)
            repository.addIncomingMessageFromFriend(
                friendId = "friend_1",
                text = "¡Acabo de subir nuevas tomas!",
                mediaUrl = "https://images.unsplash.com/photo-1518495973542-4542c06a5843?w=800&auto=format&fit=crop&q=80"
            )
        }
    }

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun openChat(friend: Friend) {
        _activeChatFriend.value = friend
    }

    fun closeChat() {
        _activeChatFriend.value = null
        _activeThreadMessage.value = null
    }

    fun openThread(message: Message) {
        _activeThreadMessage.value = message
    }

    fun closeThread() {
        _activeThreadMessage.value = null
    }

    fun startVoiceRecording() {
        _isRecordingVoice.value = true
    }

    fun cancelVoiceRecording() {
        _isRecordingVoice.value = false
    }

    fun sendVoiceRecording(durationSeconds: Int) {
        _isRecordingVoice.value = false
        val activeFriend = _activeChatFriend.value
        if (activeFriend != null) {
            repository.sendMessage(
                friendId = activeFriend.id,
                text = "Nota de voz (${durationSeconds}s)",
                audioDurationSeconds = durationSeconds
            )
        }
    }

    fun sendMessage(text: String, mediaUrl: String? = null) {
        val activeFriend = _activeChatFriend.value ?: return
        if (text.isBlank() && mediaUrl.isNullOrEmpty()) return
        repository.sendMessage(
            friendId = activeFriend.id,
            text = text.trim(),
            mediaUrl = mediaUrl
        )
    }

    fun sendInteractiveCard(card: com.example.model.InteractiveCardData) {
        val activeFriend = _activeChatFriend.value ?: return
        repository.sendMessage(
            friendId = activeFriend.id,
            text = "Tarjeta 3D: ${card.title}",
            cardData = card
        )
    }

    fun voteOnPoll(messageId: String, optionId: String) {
        val activeFriend = _activeChatFriend.value ?: return
        repository.voteOnPoll(activeFriend.id, messageId, optionId)
    }

    fun toggleCardAction(messageId: String) {
        val activeFriend = _activeChatFriend.value ?: return
        repository.toggleCardAction(activeFriend.id, messageId)
    }

    fun toggleReaction(messageId: String, emoji: String) {
        val activeFriend = _activeChatFriend.value ?: return
        repository.toggleReaction(activeFriend.id, messageId, emoji)
    }

    fun addThreadReply(messageId: String, replyText: String) {
        val activeFriend = _activeChatFriend.value ?: return
        if (replyText.isBlank()) return
        repository.addThreadReply(activeFriend.id, messageId, replyText.trim())

        // Refresh active thread message
        val updated = repository.messagesByFriend.value[activeFriend.id]?.find { it.id == messageId }
        if (updated != null) {
            _activeThreadMessage.value = updated
        }
    }

    fun setAddFriendVisible(visible: Boolean) {
        _showAddFriendDialog.value = visible
    }

    fun setEditProfileVisible(visible: Boolean) {
        _showEditProfileSheet.value = visible
    }

    fun addFriend(name: String, username: String, bio: String, avatarUrl: String) {
        repository.addFriend(name, username, bio, avatarUrl)
        _showAddFriendDialog.value = false
    }

    fun updateProfile(name: String, username: String, bio: String, statusMood: String, avatarUrl: String) {
        repository.updateProfile(name, username, bio, statusMood, avatarUrl)
        _showEditProfileSheet.value = false
    }

    fun updateSettings(newSettings: FullAppSettings) {
        repository.updateSettings(newSettings)
    }

    fun selectWallpaper(wallpaperId: String, isDark: Boolean) {
        repository.setWallpaper(wallpaperId, isDark)
    }

    fun setLanguage(language: AppLanguage) {
        repository.setLanguage(language)
    }

    fun simulateIncomingVibe() {
        val randomFriend = repository.friends.value.random()
        val sampleVibes = listOf(
            "¡Escucha este track épico! https://www.youtube.com/watch?v=jfKfPfyJRdk" to false,
            "Mira este lugar para el próximo café ☕✨" to false,
            "Nota de voz rápida de 8s" to true,
            "¿Quién se apunta a salir al atardecer? 🌅" to false
        )
        val vibe = sampleVibes.random()
        repository.addIncomingMessageFromFriend(
            friendId = randomFriend.id,
            text = vibe.first,
            isAudio = vibe.second
        )
    }
}
