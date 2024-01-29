package com.maxim.diaryforstudents.actualPerformanceSettings.presentation

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
        progressComparedSpinner: Spinner,
        sortBySpinner: Spinner
    ) {
        showProgressSwitch.isChecked = simpleStorage.read(SHOW_PROGRESS_KEY, true)
        progressComparedSpinner.setSelection(simpleStorage.read(PROGRESS_COMPARED_KEY, 0))
        sortBySpinner.setSelection(simpleStorage.read(SORT_BY_KEY, 0))
    }

    fun setShowProgress(value: Boolean) {
        simpleStorage.save(SHOW_PROGRESS_KEY, value)
    }

    fun setProgressCompared(value: Int) {
        simpleStorage.save(PROGRESS_COMPARED_KEY, value)
    }

    fun setSortBy(value: Int) {
        simpleStorage.save(SORT_BY_KEY, value)
    }

    fun close() {
        communication.reload()
        clearViewModel.clearViewModel(ActualSettingsViewModel::class.java)
    }

    companion object {
        private const val SHOW_PROGRESS_KEY = "actual_show_progress"
        private const val PROGRESS_COMPARED_KEY = "actual_progress_compared"
        private const val SORT_BY_KEY = "actual_sort_by"
    }
}