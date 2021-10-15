package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AddJCViewModel : ViewModel() {

    val labelFlow =
        MutableStateFlow(arrayOf("日常", "奇思", "物理", "数学", "文言", "高数", "功勋", "觉醒", "长津湖", "战狼", "电影"))

    fun addLabel(label: String) {
        val labelArray = labelFlow.value
        labelFlow.value = labelArray.plus(label)
    }

}