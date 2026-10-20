package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue,
    onPrimary = Color(0xFF08203B),
    secondary = SoftLavender,
    tertiary = SoftPink,
    background = Color(0xFF101827),
    surface = Color(0xFF172235),
    onBackground = Color(0xFFF3F7FF),
    onSurface = Color(0xFFF3F7FF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF329BFA),
    onPrimary = Color.White,
    primaryContainer = SkyBlueLight,
    onPrimaryContainer = Color(0xFF123B67),
    secondary = Color(0xFF8A9DFF),
    onSecondary = Color.White,
    tertiary = Color(0xFFEC8DB9),
    background = IceBlue,
    surface = Color.White,
    surfaceVariant = SkyBlueLight,
    onBackground = Ink,
    onSurface = Ink,
    onSurfaceVariant = MutedInk,
    outline = Color(0xFFD7E4F2)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // KONNICHIWA keeps its own carefully tuned palette rather than device dynamic colors.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
