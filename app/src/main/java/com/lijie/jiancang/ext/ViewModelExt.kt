package com.lijie.jiancang.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.repository.ResFlow
import com.lijie.jiancang.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


inline fun <T> ViewModel.launch(
    resFlow: ResFlow<T>,
    context: CoroutineContext = Dispatchers.IO,
    crossinline block: suspend () -> Result<T>
): Job {
    return viewModelScope.launch(context) {
        resFlow.result = Result.Loading
        val result = block.invoke()
        if (result is Result.Success) {
            resFlow.data = result.data
        }
        resFlow.result = result
    }
}