package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.ext.saveMarkdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class CollectionDetailsViewModel : ViewModel() {

    private lateinit var collectionComplete: CollectionComplete
    private var mdContent: String = ""
    private val _saved = MutableStateFlow<Boolean?>(null)
    val saved = _saved.asStateFlow()

    fun setCollectionComplete(collectionComplete: CollectionComplete) {
        this.collectionComplete = collectionComplete
    }

    fun setNewContent(content: String) {
        val type = collectionComplete.collection.type
        if (type == CollectionType.Text) {
            collectionComplete.collection.content = content
        } else if (type == CollectionType.MD) {
            mdContent = content
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val type = collectionComplete.collection.type
                if (type == CollectionType.MD) {
                    val name = File(collectionComplete.collection.content).name
                    val newFile = saveMarkdown(name, mdContent, true)
                    newFile?.let {
                        collectionComplete.collection.content = newFile.path
                    } ?: run {
                        _saved.value = false
                        return@launch
                    }
                }
                AppDatabase.db.collectionDao().update(collectionComplete.collection)
                _saved.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _saved.value = false
            }
        }
    }

}