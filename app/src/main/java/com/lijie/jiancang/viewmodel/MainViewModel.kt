package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.Content
import com.lijie.jiancang.db.entity.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(preview: Boolean = false) : ViewModel() {

    private val _collections = MutableStateFlow(
        if (preview) listOf(
            Collection(
                type = CollectionType.Text.type,
                original = "收藏",
                content = Gson().toJson(arrayListOf(Content(ContentType.Text.type, "收藏"))),
                createTime = System.currentTimeMillis()
            ),
            Collection(
                type = CollectionType.Text.type,
                original = "预览",
                content = Gson().toJson(arrayListOf(Content(ContentType.Text.type, "预览"))),
                createTime = System.currentTimeMillis()
            )
        )
        else listOf()
    )
    val collections = _collections.asStateFlow()

    fun queryCollections() {
        viewModelScope.launch(Dispatchers.IO) {
            val collectionDao = AppDatabase.db.collectionDao()
            _collections.value = collectionDao.queryCollections()
        }
    }

}