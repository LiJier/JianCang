package com.lijie.jiancang.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lijie.jiancang.ui.screen.LocalViewModel

val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun JianCangTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    ProvideWindowInsets {
        if (darkTheme) {
            LocalViewModel.current.setTheme(DarkColorPalette)
        } else {
            LocalViewModel.current.setTheme(LightColorPalette)
        }
        val theme = LocalViewModel.current.themeFlow.collectAsState()
        rememberSystemUiController().setStatusBarColor(
            Color.Transparent
        )
        MaterialTheme(
            colors = theme.value,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}