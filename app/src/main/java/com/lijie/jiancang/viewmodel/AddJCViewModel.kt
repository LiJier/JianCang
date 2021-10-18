package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.db.entity.Label
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddJCViewModel : ViewModel() {

    val labelFlow = MutableStateFlow(
        mutableListOf(
            Label(name = "预览"),
            Label(name = "预览"),
            Label(name = "预览")
        )
    )

    fun queryLabel() {
        viewModelScope.launch {
            val labelDao = AppDatabase.db.labelDao()
            val queryLabel = labelDao.queryLabel()
            if (queryLabel.isNullOrEmpty()) {
                labelDao.insert(
                    arrayListOf(
                        Label(name = "电影"),
                        Label(name = "图书"),
                        Label(name = "歌曲")
                    )
                )
            }
            labelFlow.value = labelDao.queryLabel().orEmpty().toMutableList()
        }
    }

}