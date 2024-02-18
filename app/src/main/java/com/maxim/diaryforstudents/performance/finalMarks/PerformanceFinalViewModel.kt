package com.maxim.diaryforstudents.performance.finalMarks

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.MarksType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarkViewModel
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class PerformanceFinalViewModel(
    private val interactor: PerformanceInteractor,
    private val communication: PerformanceCommunication,
    mapper: PerformanceDomain.Mapper<PerformanceUi>,
    runAsync: RunAsync = RunAsync.Base()
) : PerformanceMarkViewModel(interactor, communication, mapper, runAsync), SaveAndRestore {
    override val type = MarksType.Final

//    override fun reload() {
//        if (interactor.finalDataIsEmpty()) {
//            handle({ interactor.initFinal() }) {
//                super.reload()
//            }
//        } else
//            super.reload()
//    }

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