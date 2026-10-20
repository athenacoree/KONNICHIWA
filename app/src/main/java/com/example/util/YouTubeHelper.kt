package com.example.util

import java.util.regex.Pattern

object YouTubeHelper {
    private val YOUTUBE_PATTERN = Pattern.compile(
        "(?:https?://)?(?:www\\.)?(?:youtube\\.com/(?:[^/\\n\\s]+/.+/|(?:v|e(?:mbed)?|shorts)/|.*[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})",
        Pattern.CASE_INSENSITIVE
    )

    fun extractVideoId(text: String): String? {
        val matcher = YOUTUBE_PATTERN.matcher(text)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            null
        }
    }

    fun isYouTubeUrl(text: String): Boolean {
        return extractVideoId(text) != null
    }

    fun getThumbnailUrl(videoId: String): String {
        return "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    }

    fun getEmbedHtml(videoId: String): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body { background: #000; overflow: hidden; display: flex; align-items: center; justify-content: center; height: 100vh; }
                    iframe { width: 100%; height: 100%; border: none; }
                </style>
            </head>
            <body>
                <iframe 
                    src="https://www.youtube.com/embed/$videoId?playsinline=1&modestbranding=1&rel=0&iv_load_policy=3" 
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()
    }
}
