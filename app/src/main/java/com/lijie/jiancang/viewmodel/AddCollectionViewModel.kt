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

class AddCollectionViewModel : ViewModel() {

    private val collectionComplete = CollectionComplete(
        Collection(
            type = CollectionType.Text,
            title = "",
            content = "",
            idea = "",
        ),
        arrayListOf()
    )
    private val _labels = MutableStateFlow(listOf<Label>())
    private val _saved = MutableStateFlow<Boolean?>(null)
    val labels = _labels.asStateFlow()
    val saved = _saved.asStateFlow()

    fun setType(type: CollectionType) {
        collectionComplete.collection.type = type
    }

    fun setTitle(title: String) {
        collectionComplete.collection.title = title
    }

    fun setContent(content: String) {
        collectionComplete.collection.content = content
    }

    fun setIdea(idea: String) {
        collectionComplete.collection.idea = idea
    }

    fun setLabels(labels: List<Label>) {
        _labels.value = labels
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
            setLabels(labelDao.queryLabels().orEmpty())
        }
    }

    fun saveCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.db
                val id = db.collectionDao().insert(collectionComplete.collection)
                val collectionLabels = labels.value.filter { it.check }
                val labelQuoteList = collectionLabels.map {
                    LabelQuote(collectionId = id, labelId = it.id, labelName = it.name)
                }
                db.labelQuoteDao().insert(labelQuoteList)
                _saved.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _saved.value = false
            }
        }
    }

}