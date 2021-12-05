package com.lijie.jiancang.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ResFlow<T>(val initData: T) {

    private val _resultFlow = MutableStateFlow<Result<T>>(Result.Success(initData))
    private val _dataFlow = MutableStateFlow(initData)
    val resultFlow = _resultFlow.asStateFlow()
    val dataFlow = _dataFlow.asStateFlow()

    var result: Result<T>
        set(value) {
            _resultFlow.value = value
        }
        get() {
            return _resultFlow.value
        }

    var data: T
        set(value) {
            _dataFlow.value = value
        }
        get() {
            return _dataFlow.value
        }

}
