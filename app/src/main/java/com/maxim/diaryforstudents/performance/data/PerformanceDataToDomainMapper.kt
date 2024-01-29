package com.maxim.diaryforstudents.performance.data

import com.maxim.diaryforstudents.performance.domain.PerformanceDomain

class PerformanceDataToDomainMapper : PerformanceData.Mapper<PerformanceDomain> {
    override fun map(
        name: String,
        marks: List<PerformanceData.Mark>,
        marksSum: Int,
        isFinal: Boolean,
        average: Float,
        progress: Int
    ) = PerformanceDomain.Lesson(
        name,
        marks.map { it.map(this) as PerformanceDomain.Mark },
        marksSum,
        isFinal,
        average,
        progress
    )

    override fun map() = PerformanceDomain.Empty

    override fun map(mark: Int, date: String, isFinal: Boolean) =
        PerformanceDomain.Mark(mark, date, isFinal)

}