package com.maxim.diaryforstudents.diary.domain

import com.maxim.diaryforstudents.diary.data.MenuLessonState
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class DiaryDomainToUiMapper(
    private val mapper: PerformanceDomain.Mapper<PerformanceUi>
) : DiaryDomain.Mapper<DiaryUi> {
    override fun map(date: Int, lessons: List<DiaryDomain>) =
        DiaryUi.Day(date, lessons.map { it.map(this) })

    override fun map(
        name: String,
        number: Int,
        teacherName: String,
        topic: String,
        homework: String,
        previousHomework: String,
        startTime: String,
        endTime: String,
        date: Int,
        marks: List<PerformanceDomain.Mark>,
        absence: List<String>,
        notes: List<String>,
        menuLessonState: MenuLessonState?
    ): DiaryUi = DiaryUi.Lesson(
        name, number, teacherName, topic, homework, previousHomework, startTime, endTime, date,
        marks.map { it.map(mapper) as PerformanceUi.Mark }, absence, notes, menuLessonState
    )

    override fun map() = DiaryUi.Empty
}