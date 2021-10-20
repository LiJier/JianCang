package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.db.entity.*
import com.lijie.jiancang.db.entity.Collection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(preview: Boolean = false) : ViewModel() {

    private val _collections = MutableStateFlow(
        if (preview) listOf(
            CollectionComplete(
                Collection(
                    type = CollectionType.Text.type,
                    original = "收藏",
                    createTime = System.currentTimeMillis()
                ),
                arrayListOf(Content(type = ContentType.Text.type, content = "https://www.baidu.com https://www.qq.com", sort = 0)),
                arrayListOf(LabelQuote(labelName = "电影"))
            ),
            CollectionComplete(
                Collection(
                    type = CollectionType.Text.type,
                    original = "预览",
                    createTime = System.currentTimeMillis()
                ),
                arrayListOf(Content(type = ContentType.Text.type, content = "预览", sort = 0)),
                arrayListOf(LabelQuote(labelName = "歌曲"))
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