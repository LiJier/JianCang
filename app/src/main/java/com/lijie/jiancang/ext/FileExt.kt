package com.lijie.jiancang.ext

import ando.file.core.FileOperator
import ando.file.core.FileUtils
import android.net.Uri
import com.lijie.jiancang.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private val imageFolder by lazy {
    FileOperator.getFilesPath(FileOperator.getFileDir(), "image")
}

private val markDownFolder by lazy {
    FileOperator.getFilesPath(FileOperator.getFileDir(), "markdown")
}


@Suppress("BlockingMethodInNonBlockingContext")
suspend fun saveImage(uri: Uri): File? {
    return withContext(Dispatchers.IO) {
        imageFolder?.let { imageFolder ->
            var name = uri.pathSegments.last() ?: uri.toString()
            if (FileUtils.getExtension(name).isEmpty()) {
                name += ".jpg"
            }
            val file = File(imageFolder + name)
            App.appContext.contentResolver.openInputStream(uri)?.use {
                FileUtils.write2File(it, file, false)
            } ?: run {
                null
            }
        } ?: run {
            null
        }
    }
}

suspend fun saveMarkdown(
    title: String,
    markdown: String,
    overwrite: Boolean
): File? {
    return withContext(Dispatchers.IO) {
        markDownFolder?.let { markDownFolder ->
            val extension = FileUtils.getExtension(title)
            val name = if (extension.isEmpty()) {
                "$title.md"
            } else {
                if (extension == "md") {
                    title
                } else {
                    FileUtils.changeFileExtension(title, '.', "md")
                }
            }
            val file = File(markDownFolder + name)
            markdown.byteInputStream().use {
                FileUtils.write2File(it, file, overwrite)
            } ?: run {
                null
            }
        } ?: run {
            null
        }
    }
}