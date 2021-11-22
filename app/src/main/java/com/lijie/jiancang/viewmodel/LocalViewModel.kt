package com.lijie.jiancang.viewmodel

import androidx.compose.material.Colors
import androidx.lifecycle.ViewModel
import com.lijie.jiancang.ui.theme.LightColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalViewModel : ViewModel() {

    private val _theme = MutableStateFlow(LightColorPalette)
    val theme = _theme.asStateFlow()

    fun setTheme(colors: Colors) {
        _theme.value = colors
    }

}