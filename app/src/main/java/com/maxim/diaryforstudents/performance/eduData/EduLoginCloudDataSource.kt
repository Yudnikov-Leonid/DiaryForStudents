package com.maxim.diaryforstudents.performance.eduData

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.performance.data.PerformanceData

interface EduLoginCloudDataSource {
    suspend fun data(): List<PerformanceData>
    suspend fun finalData(): List<PerformanceData>

    class Base(private val service: DiaryService, private val storage: SimpleStorage) :
        EduLoginCloudDataSource {

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
                                grade.DATE.substring(0, grade.DATE.length - 5),
                                false
                            )
                        },
                        false, it.MARKS.sumOf { it.VALUE }.toFloat() / it.MARKS.count()
                    )
                }
            } else listOf(PerformanceData.Empty) //todo make error
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
            } else listOf(PerformanceData.Empty)
        }
    }
}