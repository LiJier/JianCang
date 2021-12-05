package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.ext.launch
import com.lijie.jiancang.repository.IRepository
import com.lijie.jiancang.repository.ResFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private lateinit var collectionComplete: CollectionComplete
    private var mdContent: String = ""
    val savedRes = ResFlow(false)

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
        launch(savedRes) {
            repository.updateCollection(collectionComplete, mdContent)
        }
    }

}