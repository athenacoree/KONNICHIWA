package com.example.data

import com.example.model.CardType
import com.example.model.CardVisualEffect
import com.example.model.Friend
import com.example.model.InteractionEvent
import com.example.model.InteractiveCardData
import com.example.model.Message
import com.example.model.MessageType
import com.example.model.PollOption
import com.example.model.ThreadReply

object SampleData {

    val friends = listOf(
        Friend(
            id = "friend_1",
            name = "Camila Ortiz",
            username = "cami_lens",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=500&auto=format&fit=crop&q=80",
            statusText = "Fotografiando la ciudad 📷",
            isOnline = true,
            lastSeenText = "En línea",
            bio = "Fotógrafa visual • Amante del café y las luces de neón ✨",
            vibeScore = 99,
            sharedMemoriesCount = 42
        ),
        Friend(
            id = "friend_2",
            name = "Mateo Castro",
            username = "mateo_beats",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop&q=80",
            statusText = "Nuevos synths listos 🎹",
            isOnline = true,
            lastSeenText = "En línea",
            bio = "Productor de música electrónica & ambient sounds 🎶",
            vibeScore = 96,
            sharedMemoriesCount = 28
        ),
        Friend(
            id = "friend_3",
            name = "Lucas Silva",
            username = "lucas_dev",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=500&auto=format&fit=crop&q=80",
            statusText = "Probando cosas nuevas 🚀",
            isOnline = false,
            lastSeenText = "hace 15 min",
            bio = "Diseñador de interacción • Explorando interfaces fluidas y 3D",
            vibeScore = 94,
            sharedMemoriesCount = 35
        ),
        Friend(
            id = "friend_4",
            name = "Sofia Mendoza",
            username = "sofia_art",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500&auto=format&fit=crop&q=80",
            statusText = "Pintando en el taller 🎨",
            isOnline = true,
            lastSeenText = "En línea",
            bio = "Artista visual • Ilustración digital y cerámica experimental",
            vibeScore = 98,
            sharedMemoriesCount = 51
        ),
        Friend(
            id = "friend_5",
            name = "Valentina Ríos",
            username = "vale_vibes",
            avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=500&auto=format&fit=crop&q=80",
            statusText = "Atardecer en la playa 🌴",
            isOnline = false,
            lastSeenText = "hace 1h",
            bio = "Arquitectura, mar y escapadas de fin de semana 🌊",
            vibeScore = 92,
            sharedMemoriesCount = 19
        ),
        Friend(
            id = "friend_6",
            name = "Diego Morales",
            username = "diego_film",
            avatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=500&auto=format&fit=crop&q=80",
            statusText = "Rodando en 35mm 🎞️",
            isOnline = false,
            lastSeenText = "hace 3h",
            bio = "Cineasta independiente • Documentando historias reales 📽️",
            vibeScore = 91,
            sharedMemoriesCount = 23
        )
    )

    fun initialMessages(friendId: String): List<Message> {
        val sampleWaveform = listOf(0.2f, 0.5f, 0.8f, 0.4f, 0.9f, 0.7f, 0.3f, 0.85f, 0.6f, 0.3f, 0.75f, 0.4f, 0.9f, 0.6f, 0.2f)

        return when (friendId) {
            "friend_1" -> listOf(
                Message(
                    id = "m1_1",
                    senderId = "friend_1",
                    recipientId = "me",
                    text = "¡Konnichiwa Alex! Te envié una tarjeta especial para este viernes:",
                    type = MessageType.TEXT,
                    formattedTime = "14:15",
                    reactions = mapOf("✨" to 2)
                ),
                Message(
                    id = "m1_card",
                    senderId = "friend_1",
                    recipientId = "me",
                    text = "Tarjeta VIP de Invitación",
                    type = MessageType.CARD,
                    cardData = InteractiveCardData(
                        id = "card_inv_1",
                        type = CardType.INVITATION,
                        effect = CardVisualEffect.COSMIC_SINGULARITY,
                        title = "Noche Cósmica en la Azotea",
                        description = "Música lofi, proyecciones visuales y amigos cercanos.",
                        details = "Viernes 20:00 • Sky Lounge Tokyo",
                        stickerEmoji = "🪐",
                        creatorName = "Camila Ortiz"
                    ),
                    formattedTime = "14:16",
                    reactions = mapOf("🔥" to 3, "❤️" to 4)
                ),
                Message(
                    id = "m1_2",
                    senderId = "friend_1",
                    recipientId = "me",
                    text = "Luz dorada de la tarde",
                    type = MessageType.IMAGE,
                    mediaUrl = "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&auto=format&fit=crop&q=80",
                    formattedTime = "14:18",
                    reactions = mapOf("❤️" to 3),
                    threadReplies = listOf(
                        ThreadReply("tr_1", "Alex Rivera", "", "¡La iluminación quedó espectacular!", "14:19", true),
                        ThreadReply("tr_2", "Camila Ortiz", "", "¡Gracias! Usé el lente anamórfico", "14:20", false)
                    )
                ),
                Message(
                    id = "m1_4",
                    senderId = "friend_1",
                    recipientId = "me",
                    text = "Nota de voz grabada",
                    type = MessageType.AUDIO,
                    audioDurationSeconds = 12,
                    audioWaveform = sampleWaveform,
                    formattedTime = "14:25",
                    reactions = mapOf("🎧" to 2)
                )
            )

            "friend_2" -> listOf(
                Message(
                    id = "m2_1",
                    senderId = "friend_2",
                    recipientId = "me",
                    text = "¡Alex! Cree esta encuesta rápida con las tarjetas interactivas de Konnichiwa:",
                    type = MessageType.TEXT,
                    formattedTime = "13:02"
                ),
                Message(
                    id = "m2_poll",
                    senderId = "friend_2",
                    recipientId = "me",
                    text = "Encuesta interactiva",
                    type = MessageType.CARD,
                    cardData = InteractiveCardData(
                        id = "card_poll_1",
                        type = CardType.POLL,
                        effect = CardVisualEffect.CYBER_NEON,
                        title = "¿Qué género producimos hoy?",
                        description = "Vota por el estilo musical de la sesión:",
                        stickerEmoji = "🎹",
                        pollOptions = listOf(
                            PollOption("p_1", "Synthwave 80s 🌌", 4),
                            PollOption("p_2", "Lofi Chill Beats ☕", 7),
                            PollOption("p_3", "Ambient Cósmico 🪐", 5)
                        ),
                        creatorName = "Mateo Castro"
                    ),
                    formattedTime = "13:03"
                ),
                Message(
                    id = "m2_2",
                    senderId = "friend_2",
                    recipientId = "me",
                    text = "https://www.youtube.com/watch?v=jfKfPfyJRdk",
                    type = MessageType.YOUTUBE,
                    youtubeVideoId = "jfKfPfyJRdk",
                    formattedTime = "13:05",
                    reactions = mapOf("🔥" to 4)
                )
            )

            else -> listOf(
                Message(
                    id = "m_default_1",
                    senderId = friendId,
                    recipientId = "me",
                    text = "¡Konnichiwa! Qué bueno encontrarte por aquí ✨",
                    type = MessageType.TEXT,
                    formattedTime = "10:30"
                )
            )
        }
    }

    val initialInteractions = listOf(
        InteractionEvent(
            id = "event_card_1",
            friend = friends[0],
            message = Message(
                id = "ie_card_1",
                senderId = "friend_1",
                recipientId = "me",
                text = "Tarjeta VIP de Invitación",
                type = MessageType.CARD,
                cardData = InteractiveCardData(
                    id = "card_inv_event",
                    type = CardType.INVITATION,
                    effect = CardVisualEffect.COSMIC_SINGULARITY,
                    title = "Noche Cósmica en la Azotea 🪐",
                    description = "Música lofi, proyecciones visuales y amigos cercanos.",
                    details = "Viernes 20:00 • Sky Lounge Tokyo",
                    stickerEmoji = "✨",
                    creatorName = "Camila Ortiz"
                ),
                formattedTime = "14:48",
                reactions = mapOf("❤️" to 5)
            ),
            timeAgo = "hace 2 min",
            isUnread = true
        ),
        InteractionEvent(
            id = "event_2",
            friend = friends[1],
            message = Message(
                id = "ie_2",
                senderId = "friend_2",
                recipientId = "me",
                text = "https://www.youtube.com/watch?v=jfKfPfyJRdk",
                type = MessageType.YOUTUBE,
                youtubeVideoId = "jfKfPfyJRdk",
                formattedTime = "14:40",
                reactions = mapOf("🔥" to 3)
            ),
            timeAgo = "hace 10 min",
            isUnread = true
        ),
        InteractionEvent(
            id = "event_3",
            friend = friends[2],
            message = Message(
                id = "ie_3",
                senderId = "friend_3",
                recipientId = "me",
                text = "Nota de voz: Las nuevas tarjetas de cristal 3D quedaron alucinantes",
                type = MessageType.AUDIO,
                audioDurationSeconds = 18,
                audioWaveform = listOf(0.3f, 0.7f, 0.5f, 0.9f, 0.4f, 0.8f, 0.6f, 0.35f, 0.75f, 0.5f),
                formattedTime = "14:25"
            ),
            timeAgo = "hace 25 min"
        )
    )
}
