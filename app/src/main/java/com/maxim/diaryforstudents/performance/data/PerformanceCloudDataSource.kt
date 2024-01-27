package com.maxim.diaryforstudents.performance.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.performance.domain.ServiceUnavailableException

interface PerformanceCloudDataSource {
    suspend fun data(quarter: Int): List<PerformanceData>
    suspend fun finalData(): List<PerformanceData>

    class Base(private val service: DiaryService, private val eduUser: EduUser) :
        PerformanceCloudDataSource {
        private val averageMap = mutableMapOf<Pair<String, Int>, Float>()

        override suspend fun data(quarter: Int): List<PerformanceData> {
            //todo hardcode
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
                    PerformanceBody(
                        BuildConfig.SHORT_API_KEY, eduUser.guid(),
                        from, to, ""
                    )
                )

            return if (data.success) {
                data.data.filter { it.MARKS.isNotEmpty() }.map {
                    PerformanceData.Lesson(
                        it.SUBJECT_NAME,
                        it.MARKS.map { mark ->
                            PerformanceData.Mark(
                                mark.VALUE,
                                mark.DATE.substring(0, mark.DATE.length - 5),
                                false
                            )
                        },
                        false, averageMap[Pair(it.SUBJECT_NAME, quarter)] ?: 0f
                    )
                }
            } else throw ServiceUnavailableException(data.message)
        }

        override suspend fun finalData(): List<PerformanceData> {
            val data = service.getFinalMarks(
                PerformanceFinalBody(
                    BuildConfig.SHORT_API_KEY,
                    eduUser.guid(),
                    ""
                )
            )

            data.data.forEach { lesson ->
                lesson.PERIODS.forEachIndexed { i, period ->
                    averageMap[Pair(lesson.NAME, i + 1)] = period.AVERAGE
                }
            }

            return if (data.success) {
                data.data.map { lesson ->
                    PerformanceData.Lesson(
                        lesson.NAME,
                        lesson.PERIODS.mapNotNull { period ->
                            period.MARK?.let { mark ->
                                PerformanceData.Mark(
                                    mark.VALUE,
                                    period.GRADE_TYPE_GUID,
                                    true
                                )
                            }
                        },
                        true, 0f
                    )
                }
            } else throw ServiceUnavailableException(data.message)
        }
    }
}