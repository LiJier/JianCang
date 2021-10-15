package com.lijie.jiancang.viewmodel

import androidx.compose.material.Colors
import androidx.lifecycle.ViewModel
import com.lijie.jiancang.ui.theme.LightColorPalette
import kotlinx.coroutines.flow.MutableStateFlow

class LocalViewModel : ViewModel() {

    val themeFlow = MutableStateFlow(LightColorPalette)

    fun setTheme(colors: Colors) {
        themeFlow.value = colors
    }

}