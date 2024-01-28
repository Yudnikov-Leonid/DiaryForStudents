package com.maxim.diaryforstudents.performance.domain

import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

class PerformanceDomainToUiMapper : PerformanceDomain.Mapper<PerformanceUi> {
    override fun map(
        name: String, marks: List<PerformanceDomain.Mark>, isFinal: Boolean, average: Float
    ) = PerformanceUi.Lesson(
        name, marks.map { it.map(this) as PerformanceUi.Mark }, isFinal, average
    )

    override fun map() = PerformanceUi.Empty

    override fun map(message: String) = PerformanceUi.Error(message)

    override fun map(mark: Int, date: String, isFinal: Boolean) =
        PerformanceUi.Mark(mark, date, isFinal)
}