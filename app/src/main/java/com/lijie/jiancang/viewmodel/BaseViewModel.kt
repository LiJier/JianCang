package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.repository.IRepository
import com.lijie.jiancang.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseViewModel constructor(val repository: IRepository) : ViewModel() {

    data class ResFlow<T>(val initData: T) {

        private val _resultFlow = MutableStateFlow<Result<T>>(Result.Success(initData))
        private val _dataFlow = MutableStateFlow(initData)
        val resultFlow = _resultFlow.asStateFlow()
        val dataFlow = _dataFlow.asStateFlow()

        var result: Result<T>
            internal set(value) {
                _resultFlow.value = value
            }
            get() {
                return _resultFlow.value
            }

        internal var data: T
            internal set(value) {
                _dataFlow.value = value
            }
            get() {
                return _dataFlow.value
            }

    }

    fun <T> launch(
        resFlow: ResFlow<T>,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend IRepository.() -> Result<T>
    ): Job {
        return viewModelScope.launch(context) {
            resFlow.result = Result.Loading
            val result = block.invoke(repository)
            if (result is Result.Success) {
                resFlow.data = result.data
            }
            resFlow.result = result
        }
    }


}