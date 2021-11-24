package com.lijie.jiancang.data.source

import android.net.Uri
import com.lijie.jiancang.data.Result
import com.lijie.jiancang.data.db.AppDatabase
import com.lijie.jiancang.data.db.entity.*
import com.lijie.jiancang.data.db.entity.Collection
import com.lijie.jiancang.ext.saveImage
import com.lijie.jiancang.ext.saveMarkdown
import okio.IOException
import java.io.File

interface ICollectionRepository {

    suspend fun getAllCollection(): Result<List<CollectionComplete>>

    suspend fun getAllLabels(): Result<List<Label>>

    suspend fun saveCollection(collection: Collection, labels: List<Label>): Result<Boolean>

    suspend fun deleteCollection(collectionComplete: CollectionComplete): Result<Boolean>

    suspend fun updateCollection(
        collectionComplete: CollectionComplete,
        mdContent: String
    ): Result<Boolean>

}

class CollectionRepository(
    private val db: AppDatabase
) : ICollectionRepository {

    override suspend fun getAllCollection(): Result<List<CollectionComplete>> {
        return try {
            val collectionDao = db.collectionDao()
            Result.Success(collectionDao.queryCollections())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    override suspend fun getAllLabels(): Result<List<Label>> {
        return try {
            val labelDao = db.labelDao()
            val queryLabel = labelDao.queryLabels()
            if (queryLabel.isNullOrEmpty()) {
                labelDao.insert(
                    arrayListOf(
                        Label(name = "电影"),
                        Label(name = "图书"),
                        Label(name = "歌曲")
                    )
                )
                Result.Success(labelDao.queryLabels())
            } else {
                Result.Success(queryLabel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

    }

    override suspend fun saveCollection(
        collection: Collection,
        labels: List<Label>
    ): Result<Boolean> {
        try {
            if (collection.type == CollectionType.Image) {
                val imageFile = saveImage(Uri.parse(collection.original))
                imageFile?.let {
                    collection.content = it.path
                } ?: run {
                    return Result.Error(IOException("文件保存失败"))
                }
            }
            if (collection.type == CollectionType.MD) {
                val mdFile = saveMarkdown(
                    collection.title ?: collection.original.substring(
                        0, if (collection.original.length > 6) 6 else collection.original.length
                    ), collection.content, false
                )
                mdFile?.let {
                    collection.content = it.path
                } ?: run {
                    return Result.Error(IOException("文件保存失败"))
                }
            }
            val id = db.collectionDao().insert(collection)
            val collectionLabels = labels.filter { label -> label.check }
            val labelQuoteList = collectionLabels.map { label ->
                LabelQuote(
                    collectionId = id,
                    labelId = label.id,
                    labelName = label.name
                )
            }
            db.labelQuoteDao().insert(labelQuoteList)
            return Result.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }

    override suspend fun deleteCollection(collectionComplete: CollectionComplete): Result<Boolean> {
        return try {
            if (collectionComplete.collection.type == CollectionType.Image || collectionComplete.collection.type == CollectionType.MD) {
                File(collectionComplete.collection.content).delete()
            }
            db.collectionDao().delete(collectionComplete.collection)
            db.labelQuoteDao().delete(collectionComplete.labelQuote)
            Result.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    override suspend fun updateCollection(
        collectionComplete: CollectionComplete,
        mdContent: String
    ): Result<Boolean> {
        try {
            val type = collectionComplete.collection.type
            if (type == CollectionType.MD) {
                val name = File(collectionComplete.collection.content).name
                val newFile = saveMarkdown(name, mdContent, true)
                newFile?.let {
                    collectionComplete.collection.content = newFile.path
                } ?: run {
                    return Result.Error(IOException("文件保存失败"))
                }
            }
            db.collectionDao().update(collectionComplete.collection)
            return Result.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }

}

class PreviewCollectionRepository : ICollectionRepository {

    override suspend fun getAllCollection(): Result<List<CollectionComplete>> {
        return Result.Success(listOf())
    }

    override suspend fun getAllLabels(): Result<List<Label>> {
        return Result.Success(arrayListOf())
    }

    override suspend fun saveCollection(
        collection: Collection,
        labels: List<Label>
    ): Result<Boolean> {
        return Result.Success(true)
    }

    override suspend fun deleteCollection(collectionComplete: CollectionComplete): Result<Boolean> {
        return Result.Success(true)
    }

    override suspend fun updateCollection(
        collectionComplete: CollectionComplete,
        mdContent: String
    ): Result<Boolean> {
        return Result.Success(true)
    }

}