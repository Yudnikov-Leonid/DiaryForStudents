package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.data.PerformanceData
import com.maxim.diaryforstudents.performance.domain.PerformanceDomain

class DiaryDataToDomainMapper(
    private val mapper: PerformanceData.Mapper<PerformanceDomain>
) : DiaryData.Mapper<DiaryDomain> {
    override fun map(date: Int, lessons: List<DiaryData>) =
        DiaryDomain.Day(date, lessons.map { it.map(this) })

    override fun map(
        name: String,
        teacherName: String,
        topic: String,
        homework: String,
        previousHomework: String,
        startTime: String,
        endTime: String,
        date: Int,
        marks: List<PerformanceData.Mark>
    ) = DiaryDomain.Lesson(
        name, teacherName, topic, homework, previousHomework, startTime, endTime, date,
        marks.map { it.map(mapper) as PerformanceDomain.Mark }
    )

    override fun map() = DiaryDomain.Empty
}