package com.maxim.diaryforstudents.performance.finalMarks.presentation

import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarkViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.MarksType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class PerformanceFinalViewModel(
    interactor: PerformanceInteractor,
    communication: PerformanceCommunication,
    mapper: PerformanceDomain.Mapper<PerformanceUi>
): PerformanceMarkViewModel(interactor, communication, mapper) {
    override val type = MarksType.Final
}