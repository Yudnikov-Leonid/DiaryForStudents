package com.maxim.diaryforstudents.performance.finalMarks

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.MarksType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarkViewModel
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PerformanceFinalViewModel @Inject constructor(
    private val interactor: PerformanceInteractor,
    private val communication: PerformanceCommunication,
    val colorManager: ColorManager,
    mapper: PerformanceDomain.Mapper<PerformanceUi>,
    runAsync: RunAsync = RunAsync.Base()
) : PerformanceMarkViewModel(interactor, communication, mapper, runAsync), SaveAndRestore {
    override val type = MarksType.Final

    override fun reload() {
        communication.update(PerformanceState.Loading)
        if (!interactor.dataIsLoading { super.reload() })
            super.reload()
    }

    //not tested
    fun retry() {
        handle ({ interactor.loadData() } ) {
            reload()
        }
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
    }

    companion object {
        private const val RESTORE_KEY = "performance_final_communication_key"
    }
}