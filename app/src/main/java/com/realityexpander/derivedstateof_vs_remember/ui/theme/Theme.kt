package com.realityexpander.derivedstateof_vs_remember.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0059CB),
    onPrimary = Color(0xFFDDDDDD),
    secondary = Color(0xFF4cb2a2),
    onSecondary = Color(0xFFDDDDDD),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFBBBBBB),
    background = Color(0xFF333344),
    onBackground = Color(0xFFDDDDDD),
    onError = Color(0xFF222222),
    error = Color(0xFFFF0000)
)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0089FB),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF00917d),
    onSecondary = Color(0xFFFFFFFF),
    // tertiary = Color(0xFF3700B3)
    surface = Color(0xFFCCCCCC),
    onSurface = Color(0xFF000000),
    background = Color(0xFF444444),
    onBackground = Color(0xFFDDDDDD),
    onError = Color(0xFF222222),
    error = Color(0xFFFF0000)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


@Composable
fun DerivedStateOf_vs_RememberTheme(
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

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
