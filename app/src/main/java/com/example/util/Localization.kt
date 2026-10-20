package com.example.util

import com.example.model.AppLanguage

object Strings {
    fun interactions(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Interacciones"
        AppLanguage.ENGLISH -> "Interactions"
        AppLanguage.JAPANESE -> "交流ストリーム"
    }

    fun chats(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Chats"
        AppLanguage.ENGLISH -> "Chats"
        AppLanguage.JAPANESE -> "チャット"
    }

    fun profile(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Perfil"
        AppLanguage.ENGLISH -> "Profile"
        AppLanguage.JAPANESE -> "プロフィール"
    }

    fun settings(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Ajustes"
        AppLanguage.ENGLISH -> "Settings"
        AppLanguage.JAPANESE -> "設定"
    }

    fun accessibility(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Accesibilidad"
        AppLanguage.ENGLISH -> "Accessibility"
        AppLanguage.JAPANESE -> "アクセシビリティ"
    }

    fun privacy(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Privacidad"
        AppLanguage.ENGLISH -> "Privacy"
        AppLanguage.JAPANESE -> "プライバシー"
    }

    fun security(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Seguridad"
        AppLanguage.ENGLISH -> "Security"
        AppLanguage.JAPANESE -> "セキュリティ"
    }

    fun style(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Estilo & Fondos"
        AppLanguage.ENGLISH -> "Style & Wallpapers"
        AppLanguage.JAPANESE -> "スタイルと壁紙"
    }

    fun online(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "en línea"
        AppLanguage.ENGLISH -> "online"
        AppLanguage.JAPANESE -> "オンライン"
    }

    fun writeMessage(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Escribe un mensaje..."
        AppLanguage.ENGLISH -> "Write a message..."
        AppLanguage.JAPANESE -> "メッセージを入力..."
    }

    fun holdToRecord(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Mantén presionado para hablar"
        AppLanguage.ENGLISH -> "Hold to record audio"
        AppLanguage.JAPANESE -> "長押しで音声録音"
    }

    fun releaseToSend(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Suelta para enviar nota de voz"
        AppLanguage.ENGLISH -> "Release to send voice note"
        AppLanguage.JAPANESE -> "離してボイスメモ送信"
    }

    fun threadReplies(count: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "$count respuestas en hilo"
        AppLanguage.ENGLISH -> "$count thread replies"
        AppLanguage.JAPANESE -> "$count 件のスレッド返信"
    }

    fun openThread(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Abrir hilo"
        AppLanguage.ENGLISH -> "Open thread"
        AppLanguage.JAPANESE -> "スレッドを開く"
    }

    fun addFriend(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Agregar amigo"
        AppLanguage.ENGLISH -> "Add friend"
        AppLanguage.JAPANESE -> "友達を追加"
    }

    fun existingFriends(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Tus amigos"
        AppLanguage.ENGLISH -> "Your friends"
        AppLanguage.JAPANESE -> "あなたの友達"
    }

    fun editProfile(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Editar perfil"
        AppLanguage.ENGLISH -> "Edit profile"
        AppLanguage.JAPANESE -> "プロフィール編集"
    }

    fun save(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Guardar cambios"
        AppLanguage.ENGLISH -> "Save changes"
        AppLanguage.JAPANESE -> "変更を保存"
    }

    fun cancel(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Cancelar"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.JAPANESE -> "キャンセル"
    }

    fun lightWallpapers(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "12 Fondos Claros"
        AppLanguage.ENGLISH -> "12 Light Wallpapers"
        AppLanguage.JAPANESE -> "12種類のライト壁紙"
    }

    fun darkWallpapers(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "12 Fondos Oscuros"
        AppLanguage.ENGLISH -> "12 Dark Wallpapers"
        AppLanguage.JAPANESE -> "12種類のダーク壁紙"
    }

    fun languageTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.SPANISH -> "Idioma de la app"
        AppLanguage.ENGLISH -> "App Language"
        AppLanguage.JAPANESE -> "言語設定"
    }
}
