package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import java.util.Calendar

interface PerformanceCloudDataSource {
    suspend fun data(quarter: Int, calculateProgress: Boolean): List<PerformanceData>
    suspend fun finalData(): List<PerformanceData>

    class Base(private val service: DiaryService, private val eduUser: EduUser) :
        PerformanceCloudDataSource {
        private val averageMap = mutableMapOf<Pair<String, Int>, Float>()

        override suspend fun data(quarter: Int, calculateProgress: Boolean): List<PerformanceData> {
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
                data.data.filter { it.MARKS.isNotEmpty() }.map { lesson ->
                    val progresses = IntArray(3)
                    val actualAverage = averageMap[Pair(lesson.SUBJECT_NAME, quarter)] ?: 0f
                    if (calculateProgress) {
                        listOf(7, 14, 28).forEachIndexed { i, n ->
                            val aWeekAgo = System.currentTimeMillis() / 86400000 - n
                            val calendar = Calendar.getInstance()
                            val marksAWeekAgo = lesson.MARKS.filter { mark ->
                                val markDate = mark.DATE.split('.')
                                calendar.set(Calendar.DAY_OF_MONTH, markDate[0].toInt())
                                calendar.set(Calendar.MONTH, markDate[1].toInt() - 1)
                                calendar.set(Calendar.YEAR, markDate[2].toInt())
                                calendar.timeInMillis / 86400000 <= aWeekAgo
                            }
                            val average =
                                marksAWeekAgo.sumOf { it.VALUE }.toFloat() / marksAWeekAgo.size
                            progresses[i] = ((actualAverage / average - 1) * 100).toInt()
                        }
                    }

                    PerformanceData.Lesson(
                        lesson.SUBJECT_NAME,
                        lesson.MARKS.map { mark ->
                            PerformanceData.Mark(
                                mark.VALUE,
                                mark.DATE.substring(0, mark.DATE.length - 5),
                                false
                            )
                        },
                        lesson.MARKS.sumOf { it.VALUE },
                        false,
                        actualAverage,
                        progresses[0],
                        progresses[1],
                        progresses[2],
                        if (calculateProgress) (averageMap[Pair(
                            lesson.SUBJECT_NAME,
                            quarter - 1
                        )]?.let {
                            (actualAverage / it - 1) * 100
                        } ?: 0).toInt() else 0
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
                        }, 0,
                        true, 0f, 0, 0, 0, 0
                    )
                }
            } else throw ServiceUnavailableException(data.message)
        }
    }
}