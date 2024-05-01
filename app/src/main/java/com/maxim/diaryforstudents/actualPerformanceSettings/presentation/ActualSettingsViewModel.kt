package com.maxim.diaryforstudents.actualPerformanceSettings.presentation

import android.view.View
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActualSettingsViewModel @Inject constructor(
    private val simpleStorage: SimpleStorage,
    private val clearViewModel: ClearViewModel
) : ViewModel() {

    fun init(
        showProgressSwitch: SwitchCompat,
        showTypeSwitch: SwitchCompat,
        progressInfo: View,
        typeInfo: View,
        progressComparedSpinner: Spinner,
        sortBySpinner: Spinner,
        sortingOrderSpinner: Spinner
    ) {
        showProgressSwitch.isChecked = simpleStorage.read(SHOW_PROGRESS_KEY, true)
        showTypeSwitch.isChecked = simpleStorage.read(SHOW_TYPE_KEY, true)
        progressInfo.visibility = if (showProgressSwitch.isChecked) View.VISIBLE else View.GONE
        typeInfo.visibility = if (showTypeSwitch.isChecked) View.VISIBLE else View.GONE
        progressComparedSpinner.setSelection(simpleStorage.read(PROGRESS_COMPARED_KEY, 0))
        sortBySpinner.setSelection(simpleStorage.read(SORT_BY_KEY, 0))
        sortingOrderSpinner.setSelection(simpleStorage.read(SORTING_ORDER_KEY, 0))
    }

    fun setShowType(value: Boolean, reload: () -> Unit) {
        simpleStorage.save(SHOW_TYPE_KEY, value)
        reload.invoke()
    }

    fun setShowProgress(value: Boolean, reload: () -> Unit) {
        simpleStorage.save(SHOW_PROGRESS_KEY, value)
        reload.invoke()
    }

    fun setProgressCompared(value: Int, reload: () -> Unit) {
        simpleStorage.save(PROGRESS_COMPARED_KEY, value)
        reload.invoke()
    }

    fun setSortBy(value: Int, reload: () -> Unit) {
        simpleStorage.save(SORT_BY_KEY, value)
        reload.invoke()
    }

    fun setSortingOrder(value: Int, reload: () -> Unit) {
        simpleStorage.save(SORTING_ORDER_KEY, value)
        reload.invoke()
    }

    fun close() {
        clearViewModel.clearViewModel(ActualSettingsViewModel::class.java)
    }

    companion object {
        const val SHOW_PROGRESS_KEY = "actual_show_progress"
        const val SHOW_TYPE_KEY = "actual_show_type"
        const val PROGRESS_COMPARED_KEY = "actual_progress_compared"
        const val SORT_BY_KEY = "actual_sort_by"
        const val SORTING_ORDER_KEY = "actual_sorting_order"
    }
}