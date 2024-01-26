package com.maxim.diaryforstudents.performance.eduData

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.performance.data.PerformanceData

interface EduPerformanceCloudDataSource {
    suspend fun data(quarter: Int): List<PerformanceData>
    suspend fun finalData(): List<PerformanceData>

    class Base(private val service: DiaryService, private val storage: SimpleStorage) :
        EduPerformanceCloudDataSource {

        override suspend fun data(quarter: Int): List<PerformanceData> {
            val from = when (quarter) {
                1 -> "01.09.2023"
                2 -> "06.11.2023"
                3 -> "09.01.2024"
                else -> "01.04.2024"
            }
            val to = when (quarter) {
                1 -> "27.10.2023"
                2 -> "29.12.2023"
                3 -> "22.03.2024"
                else -> "26.05.2024"
            }

            val data =
                service.getMarks(
                    EduPerformanceBody(
                        "SRJTDhppUiI", storage.read("GUID", ""),
                        from, to, ""
                    )
                )

            return if (data.success) {
                data.data.filter { it.MARKS.isNotEmpty() }.map {
                    PerformanceData.Lesson(
                        it.SUBJECT_NAME,
                        it.MARKS.map { grade ->
                            PerformanceData.Grade(
                                grade.VALUE,
                                grade.DATE.substring(0, grade.DATE.length - 5),
                                false
                            )
                        },
                        false, it.MARKS.sumOf { it.VALUE }.toFloat() / it.MARKS.count()
                    )
                }
            } else listOf(PerformanceData.Error(data.message))
        }

        override suspend fun finalData(): List<PerformanceData> {
            val data = service.getFinalMarks(
                EduPerformanceFinalBody(
                    "SRJTDhppUiI",
                    storage.read("GUID", ""),
                    ""
                )
            )

            return if (data.success) {
                data.data.map { lesson ->
                    PerformanceData.Lesson(
                        lesson.NAME,
                        lesson.PERIODS.mapNotNull { period ->
                            period.MARK?.let { mark ->
                                PerformanceData.Grade(
                                    mark.VALUE,
                                    period.GRADE_TYPE_GUID,
                                    true
                                )
                            }
                        },
                        true, 0f
                    )
                }
            } else listOf(PerformanceData.Error(data.message))
        }
    }
}