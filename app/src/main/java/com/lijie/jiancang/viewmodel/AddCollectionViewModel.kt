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

class AddCollectionViewModel(preview: Boolean = false) : ViewModel() {

    private val _contents = MutableStateFlow(arrayListOf<Content>())
    private val _labels = MutableStateFlow(
        if (preview)
            listOf(
                Label(name = "预览"),
                Label(name = "预览"),
                Label(name = "预览")
            )
        else listOf()
    )
    private val _idea = MutableStateFlow("")
    private val _save = MutableStateFlow<Boolean?>(null)
    val contents = _contents.asStateFlow()
    val labels = _labels.asStateFlow()
    val idea = _idea.asStateFlow()
    val save = _save.asStateFlow()
    var original: String = ""
    var type: CollectionType = CollectionType.Text

    fun addContent(type: ContentType, content: String) {
        val contents = _contents.value
        contents.add(Content(type = type, content = content, sort = contents.size))
        _contents.value = contents
    }

    fun queryLabel() {
        viewModelScope.launch(Dispatchers.IO) {
            val labelDao = AppDatabase.db.labelDao()
            val queryLabel = labelDao.queryLabels()
            if (queryLabel.isNullOrEmpty()) {
                labelDao.insert(
                    arrayListOf(
                        Label(name = "电影"),
                        Label(name = "图书"),
                        Label(name = "歌曲")
                    )
                )
            }
            _labels.value = labelDao.queryLabels().orEmpty()
        }
    }

    fun setIdea(idea: String) {
        _idea.value = idea
    }

    fun saveCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.db
                val collection = Collection(
                    type = CollectionType.Text,
                    original = original,
                    idea = idea.value,
                    createTime = System.currentTimeMillis()
                )
                val id = db.collectionDao().insert(collection)
                val contents = _contents.value.map {
                    it.collectionId = id
                    it
                }
                db.contentDao().insert(contents)
                val collectionLabels = labels.value.filter { it.check }
                val labelQuoteList = collectionLabels.map {
                    LabelQuote(collectionId = id, labelId = it.id, labelName = it.name)
                }
                db.labelQuoteDao().insert(labelQuoteList)
                _save.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _save.value = false
            }
        }
    }

}