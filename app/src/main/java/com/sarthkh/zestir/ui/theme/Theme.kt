package com.sarthkh.zestir.ui.theme

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

private val ZestirLightGreen = Color(0xFFA0FF00)
private val ZestirDarkGreen = Color(0xFF008f11)

// light theme colors
private val md_theme_light_primary = ZestirDarkGreen
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_primaryContainer = Color(0xFFADF643)
private val md_theme_light_onPrimaryContainer = Color(0xFF0C2000)
private val md_theme_light_secondary = Color(0xFF57624A)
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFFDBE7C8)
private val md_theme_light_onSecondaryContainer = Color(0xFF151E0B)
private val md_theme_light_tertiary = Color(0xFF386665)
private val md_theme_light_onTertiary = Color(0xFFFFFFFF)
private val md_theme_light_tertiaryContainer = Color(0xFFBBECEA)
private val md_theme_light_onTertiaryContainer = Color(0xFF002020)
private val md_theme_light_error = Color(0xFFBA1A1A)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_errorContainer = Color(0xFFFFDAD6)
private val md_theme_light_onErrorContainer = Color(0xFF410002)
private val md_theme_light_background = Color(0xFFFDFDF5)
private val md_theme_light_onBackground = Color(0xFF1A1C17)
private val md_theme_light_surface = Color(0xFFFDFDF5)
private val md_theme_light_onSurface = Color(0xFF1A1C17)

// dark theme colors
private val md_theme_dark_primary = ZestirLightGreen
private val md_theme_dark_onPrimary = Color(0xFF173800)
private val md_theme_dark_primaryContainer = Color(0xFF245100)
private val md_theme_dark_onPrimaryContainer = Color(0xFFADF643)
private val md_theme_dark_secondary = Color(0xFFBFCBAD)
private val md_theme_dark_onSecondary = Color(0xFF2A331F)
private val md_theme_dark_secondaryContainer = Color(0xFF404A34)
private val md_theme_dark_onSecondaryContainer = Color(0xFFDBE7C8)
private val md_theme_dark_tertiary = Color(0xFFA0CFCE)
private val md_theme_dark_onTertiary = Color(0xFF003737)
private val md_theme_dark_tertiaryContainer = Color(0xFF1E4E4D)
private val md_theme_dark_onTertiaryContainer = Color(0xFFBBECEA)
private val md_theme_dark_error = Color(0xFFFFB4AB)
private val md_theme_dark_onError = Color(0xFF690005)
private val md_theme_dark_errorContainer = Color(0xFF93000A)
private val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
private val md_theme_dark_background = Color(0xFF1A1A1A)
private val md_theme_dark_onBackground = Color(0xFFE3E3DB)
private val md_theme_dark_surface = Color(0xFF1A1C17)
private val md_theme_dark_onSurface = Color(0xFFE3E3DB)

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface
)

@Composable
fun ZestirTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = zestirTypography,
        content = content
    )
}