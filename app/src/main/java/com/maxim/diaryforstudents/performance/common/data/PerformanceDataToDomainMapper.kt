package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain

class PerformanceDataToDomainMapper : PerformanceData.Mapper<PerformanceDomain> {
    override fun map(
        name: String,
        marks: List<PerformanceData>,
        marksSum: Int,
        isFinal: Boolean,
        average: Float,
        weekProgress: Int,
        twoWeeksProgress: Int,
        monthProgress: Int,
        quarterProgress: Int
    ): PerformanceDomain = PerformanceDomain.Lesson(
        name,
        marks.map { it.map(this) },
        marksSum,
        isFinal,
        average,
        weekProgress, twoWeeksProgress, monthProgress, quarterProgress
    )

    override fun map() = PerformanceDomain.Empty

    override fun map(mark: Int, date: String, lessonName: String, isFinal: Boolean) =
        PerformanceDomain.Mark(mark, date, lessonName, isFinal)

    override fun map(
        marks: List<Int>,
        date: String,
        lessonName: String,
    ): PerformanceDomain =
        PerformanceDomain.SeveralMarks(marks, date, lessonName)


}