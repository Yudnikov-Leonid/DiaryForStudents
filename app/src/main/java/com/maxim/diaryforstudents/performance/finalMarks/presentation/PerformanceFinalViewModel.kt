package com.maxim.diaryforstudents.performance.finalMarks.presentation

import com.maxim.diaryforstudents.performance.common.PerformanceMarkViewModel
import com.maxim.diaryforstudents.performance.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.presentation.MarksType
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

class PerformanceFinalViewModel(
    interactor: PerformanceInteractor,
    communication: PerformanceCommunication,
    mapper: PerformanceDomain.Mapper<PerformanceUi>
): PerformanceMarkViewModel(interactor, communication, mapper) {
    override val type = MarksType.Final
}