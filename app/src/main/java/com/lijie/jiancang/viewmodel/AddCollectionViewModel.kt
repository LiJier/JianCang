package com.lijie.jiancang.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.db.entity.LabelQuote
import com.lijie.jiancang.ext.saveImage
import com.lijie.jiancang.ext.saveMarkdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddCollectionViewModel : ViewModel() {

    private val collection = Collection(
        type = CollectionType.Text,
        original = "",
        title = "",
        content = "",
        idea = "",
    )

    private val _labels = MutableStateFlow(listOf<Label>())
    private val _saved = MutableStateFlow<Boolean?>(null)
    val labels = _labels.asStateFlow()
    val saved = _saved.asStateFlow()

    fun setType(type: CollectionType) {
        collection.type = type
    }

    fun setOriginal(original: String) {
        collection.original = original
    }

    fun setTitle(title: String) {
        collection.title = title
    }

    fun setContent(content: String) {
        collection.content = content
    }

    fun setIdea(idea: String) {
        collection.idea = idea
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
                if (collection.type == CollectionType.Image) {
                    val imageFile = saveImage(Uri.parse(collection.original))
                    imageFile?.let {
                        setContent(it.path)
                    } ?: run {
                        _saved.value = false
                        return@launch
                    }
                }
                if (collection.type == CollectionType.MD) {
                    val mdFile = saveMarkdown(
                        collection.title ?: collection.original.substring(
                            0, if (collection.original.length > 6) 6 else collection.original.length
                        ), collection.content, false
                    )
                    mdFile?.let {
                        setContent(it.path)
                    } ?: run {
                        _saved.value = false
                        return@launch
                    }
                }
                val db = AppDatabase.db
                val id = db.collectionDao().insert(collection)
                val collectionLabels = labels.value.filter { label -> label.check }
                val labelQuoteList = collectionLabels.map { label ->
                    LabelQuote(
                        collectionId = id,
                        labelId = label.id,
                        labelName = label.name
                    )
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