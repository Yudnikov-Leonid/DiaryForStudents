package com.maxim.diaryforstudents.actualPerformanceSettings.presentation

import android.view.View
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.sl.ClearViewModel

class ActualSettingsViewModel(
    private val communication: SaveActualSettingsCommunication.Write,
    private val simpleStorage: SimpleStorage,
    private val clearViewModel: ClearViewModel
) : ViewModel() {

    fun init(
        showProgressSwitch: SwitchCompat,
        progressInfo: View,
        progressComparedSpinner: Spinner,
        sortBySpinner: Spinner,
        sortingOrderSpinner: Spinner
    ) {
        showProgressSwitch.isChecked = simpleStorage.read(SHOW_PROGRESS_KEY, true)
        progressInfo.visibility = if (showProgressSwitch.isChecked) View.VISIBLE else View.GONE
        progressComparedSpinner.setSelection(simpleStorage.read(PROGRESS_COMPARED_KEY, 0))
        sortBySpinner.setSelection(simpleStorage.read(SORT_BY_KEY, 0))
        sortingOrderSpinner.setSelection(simpleStorage.read(SORTING_ORDER_KEY, 0))
    }

    fun setShowProgress(value: Boolean) {
        simpleStorage.save(SHOW_PROGRESS_KEY, value)
        communication.reload()
    }

    fun setProgressCompared(value: Int) {
        simpleStorage.save(PROGRESS_COMPARED_KEY, value)
        communication.reload()
    }

    fun setSortBy(value: Int) {
        simpleStorage.save(SORT_BY_KEY, value)
        communication.reload()
    }

    fun setSortingOrder(value: Int) {
        simpleStorage.save(SORTING_ORDER_KEY, value)
        communication.reload()
    }

    fun close() {
        clearViewModel.clearViewModel(ActualSettingsViewModel::class.java)
    }

    companion object {
        const val SHOW_PROGRESS_KEY = "actual_show_progress"
        const val PROGRESS_COMPARED_KEY = "actual_progress_compared"
        const val SORT_BY_KEY = "actual_sort_by"
        const val SORTING_ORDER_KEY = "actual_sorting_order"
    }
}