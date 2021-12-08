package com.lijie.jiancang.repository

import android.net.Uri
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.db.entity.*
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.ext.saveImage
import com.lijie.jiancang.ext.saveMarkdown
import okio.IOException
import java.io.File

interface IRepository {

    suspend fun getAllCollection(): Result<List<CollectionComplete>>

    suspend fun getAllLabels(): Result<List<Label>>

    suspend fun saveCollection(collection: Collection, labels: List<Label>): Result<Boolean>

    suspend fun deleteCollection(collectionComplete: CollectionComplete): Result<Boolean>

    suspend fun updateCollection(
        collectionComplete: CollectionComplete,
        mdContent: String
    ): Result<Boolean>

    suspend fun addLabel(labelName: String): Result<Boolean>

    suspend fun deleteLabel(label: Label): Result<Boolean>

    suspend fun getLabelCollections(labelId: Long): Result<List<CollectionComplete>>

}

class Repository(
    private val db: AppDatabase
) : IRepository {

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

    override suspend fun addLabel(labelName: String): Result<Boolean> {
        return try {
            val labelDao = db.labelDao()
            labelDao.insert(Label(name = labelName))
            Result.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    override suspend fun deleteLabel(label: Label): Result<Boolean> {
        return try {
            db.labelQuoteDao().deleteLabel(label.id)
            db.labelDao().delete(label)
            Result.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    override suspend fun getLabelCollections(labelId: Long): Result<List<CollectionComplete>> {
        return try {
            val allCollection = db.collectionDao().queryCollections()
            Result.Success(allCollection.filter {
                val labelQuotes = it.labelQuote
                return@filter labelQuotes.find { it1 -> it1.labelId == labelId } != null
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

}

object PreviewRepository : IRepository {

    override suspend fun getAllCollection(): Result<List<CollectionComplete>> {
        return Result.Success(
            listOf(
                CollectionComplete(
                    Collection(
                        type = CollectionType.URL,
                        original = "",
                        title = "百度",
                        content = "https://www.baidu.com/",
                        createTime = System.currentTimeMillis()
                    ),
                    arrayListOf(LabelQuote(labelName = "电影"))
                ),
                CollectionComplete(
                    Collection(
                        type = CollectionType.Text,
                        original = "",
                        title = "标题",
                        content = "预览",
                        createTime = System.currentTimeMillis()
                    ),
                    arrayListOf(LabelQuote(labelName = "歌曲"))
                )
            )
        )
    }

    override suspend fun getAllLabels(): Result<List<Label>> {
        return Result.Success(
            listOf(
                Label(name = "电影"),
                Label(name = "图书"),
                Label(name = "歌曲")
            )
        )
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

    override suspend fun addLabel(labelName: String): Result<Boolean> {
        return Result.Success(true)
    }

    override suspend fun deleteLabel(label: Label): Result<Boolean> {
        return Result.Success(true)
    }

    override suspend fun getLabelCollections(labelId: Long): Result<List<CollectionComplete>> {
        return Result.Success(
            listOf(
                CollectionComplete(
                    Collection(
                        type = CollectionType.URL,
                        original = "",
                        title = "百度",
                        content = "https://www.baidu.com/",
                        createTime = System.currentTimeMillis()
                    ),
                    arrayListOf(LabelQuote(labelName = "电影"))
                ),
                CollectionComplete(
                    Collection(
                        type = CollectionType.Text,
                        original = "",
                        title = "标题",
                        content = "预览",
                        createTime = System.currentTimeMillis()
                    ),
                    arrayListOf(LabelQuote(labelName = "歌曲"))
                )
            )
        )
    }

}