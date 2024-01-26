package com.maxim.diaryforstudents.performance.eduData

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.performance.data.PerformanceData

interface EduPerformanceRepository {
    suspend fun data(): List<PerformanceData>

    class Base(private val service: DiaryService, private val storage: SimpleStorage) :
        EduPerformanceRepository {
        override suspend fun data(): List<PerformanceData> {
            val data =
                service.getMarks(
                    EduPerformanceBody(
                        "SRJTDhppUiI", storage.read("GUID", ""),
                        "09.01.2024", "22.03.2024", ""
                    )
                )

            return if (data.success) {
                data.data.filter { it.MARKS.isNotEmpty() }.map {
                    PerformanceData.Lesson(
                        it.SUBJECT_NAME,
                        it.MARKS.map { grade ->
                            PerformanceData.Grade(
                                grade.VALUE,
                                grade.DATE.substring(0, grade.DATE.length - 5)
                            )
                        },
                        it.MARKS.sumOf { it.VALUE }.toFloat() / it.MARKS.count()
                    )
                }
            } else listOf(PerformanceData.Empty)
        }
    }
}