package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Lightweight frosted-glass surface. Keep the content readable and use a clear rim,
 * soft tint and layered shadow instead of a dark opaque panel in light mode.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(26.dp),
    isDark: Boolean = false,
    alpha: Float = 0.72f,
    elevation: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val safeAlpha = alpha.coerceIn(0.2f, 0.96f)
    val backgroundColor = if (isDark) {
        Color(0xFF172235).copy(alpha = safeAlpha)
    } else {
        Color(0xFFFDFEFF).copy(alpha = safeAlpha)
    }

    val rimGradient = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.32f),
                Color(0xFF8ABEFF).copy(alpha = 0.10f),
                Color.White.copy(alpha = 0.04f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.98f),
                Color(0xFFB9DEFF).copy(alpha = 0.82f),
                Color.White.copy(alpha = 0.58f)
            )
        )
    }

    val shadowColor = if (isDark) {
        Color.Black.copy(alpha = 0.30f)
    } else {
        Color(0xFF6C9AC8).copy(alpha = 0.19f)
    }

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(
                color = if (isDark) Color.White.copy(alpha = 0.16f)
                else Color(0xFF329BFA).copy(alpha = 0.16f)
            ),
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(shape)
            .background(backgroundColor)
            .border(borderWidth, rimGradient, shape)
            .then(clickableModifier),
        content = content
    )
}
