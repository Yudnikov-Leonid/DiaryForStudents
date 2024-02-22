package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.performance.common.presentation.MarkType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class PerformanceDomainToUiMapper : PerformanceDomain.Mapper<PerformanceUi> {
    override fun map(
        name: String,
        marks: List<PerformanceDomain>,
        marksSum: Int,
        isFinal: Boolean,
        average: Float,
        weekProgress: Int,
        twoWeeksProgress: Int,
        monthProgress: Int,
        quarterProgress: Int
    ): PerformanceUi = PerformanceUi.Lesson(
        name,
        marks.map { it.map(this) },
        marksSum,
        isFinal,
        average,
        weekProgress,
        twoWeeksProgress,
        monthProgress,
        quarterProgress
    )

    override fun map() = PerformanceUi.Empty

    override fun map(message: String) = PerformanceUi.Error(message)

    override fun map(
        mark: Int,
        type: MarkType,
        date: String,
        lessonName: String,
        isFinal: Boolean
    ): PerformanceUi =
        PerformanceUi.Mark(mark, type, date, lessonName, isFinal)

    override fun map(
        marks: List<Int>,
        types: List<MarkType>,
        date: String,
        lessonName: String
    ): PerformanceUi = PerformanceUi.SeveralMarks(marks, types, date, lessonName)
}