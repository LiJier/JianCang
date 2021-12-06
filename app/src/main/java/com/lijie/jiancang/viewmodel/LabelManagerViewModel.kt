package com.lijie.jiancang.viewmodel

import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.repository.IRepository
import com.lijie.jiancang.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LabelManagerViewModel @Inject constructor(repository: IRepository) :
    BaseViewModel(repository) {

    val labelsRes = ResFlow<List<Label>>(listOf())

    val addDeleteLabelRes = ResFlow(false)

    fun queryLabel() {
        launch(labelsRes) {
            getAllLabels()
        }
    }

    fun addLabel(labelName: String) {
        launch(addDeleteLabelRes) {
            val result = repository.addLabel(labelName)
            if (result is Result.Success) {
                queryLabel()
            }
            result
        }
    }

    fun deleteLabel(label: Label) {
        launch(addDeleteLabelRes) {
            val result = deleteLabel(label)
            if (result is Result.Success) {
                queryLabel()
            }
            result
        }
    }

}