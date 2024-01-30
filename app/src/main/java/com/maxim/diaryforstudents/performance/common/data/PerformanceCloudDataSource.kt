package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.performance.analytics.data.AnalyticsData
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import java.util.Calendar

interface PerformanceCloudDataSource {
    suspend fun data(quarter: Int, calculateProgress: Boolean): List<PerformanceData>
    suspend fun analytics(quarter: Int): List<AnalyticsData>
    suspend fun finalData(): List<PerformanceData>

    class Base(private val service: PerformanceService, private val eduUser: EduUser) :
        PerformanceCloudDataSource {
        private val averageMap = mutableMapOf<Pair<String, Int>, Float>()
        private val actualCacheMap = mutableMapOf<Int, PerformanceResponse>()

        //todo
        private fun dates(quarter: Int): Pair<String, String> =
            when (quarter) {
                1 -> Pair("01.09.2023", "27.10.2023")
                2 -> Pair("06.11.2023", "29.12.2023")
                3 -> Pair("09.01.2024", "22.03.2024")
                4 -> Pair("01.04.2024", "26.05.2024")
                else -> Pair("01.09.2023", "26.05.2024")
            }

        override suspend fun data(quarter: Int, calculateProgress: Boolean): List<PerformanceData> {
            val dates = dates(quarter)
            val data = service.getMarks(
                PerformanceBody(
                    BuildConfig.SHORT_API_KEY, eduUser.guid(),
                    dates.first, dates.second, ""
                )
            )
            actualCacheMap[quarter] = data

            return if (data.success) {
                actualCacheMap.clear()
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
                                mark.DATE,
                                lesson.SUBJECT_NAME,
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

        override suspend fun analytics(quarter: Int): List<AnalyticsData> {
            val dates = dates(quarter)
            val data = actualCacheMap[quarter] ?: service.getMarks(
                PerformanceBody(
                    BuildConfig.SHORT_API_KEY, eduUser.guid(),
                    dates.first, dates.second, ""
                )
            )
            if (actualCacheMap[quarter] == null)
                actualCacheMap[quarter] = data

            if (data.success) {
                val marks = mutableListOf<CloudMark>()
                data.data.forEach { lesson ->
                    marks.addAll(lesson.MARKS)
                }

                val fiveCount = marks.filter { it.VALUE == 5 }.size
                val fourCount = marks.filter { it.VALUE == 4 }.size
                val threeCount = marks.filter { it.VALUE == 3 }.size
                val twoCount = marks.filter { it.VALUE == 2 }.size

                //calculate weeks count
                var firstDate: Int
                var lastDate: Int
                val calendar = Calendar.getInstance()
                calendar.apply {
                    var split = dates.first.split('.')
                    set(Calendar.DAY_OF_MONTH, split[0].toInt())
                    set(Calendar.MONTH, split[1].toInt())
                    set(Calendar.YEAR, split[2].toInt())
                    firstDate = (timeInMillis / 86400000).toInt()
                    split = dates.second.split('.')
                    set(Calendar.DAY_OF_MONTH, split[0].toInt())
                    set(Calendar.MONTH, split[1].toInt())
                    set(Calendar.YEAR, split[2].toInt())
                    lastDate = (timeInMillis / 86400000).toInt()

                }
                val weeksCount = (lastDate - firstDate) / 7

                val result = mutableListOf<Float>()
                val separateMarksResult = listOf<java.util.ArrayList<Float>>(
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    ArrayList()
                )
                val labels = mutableListOf<String>()

                for (i in 1..weeksCount) {
                    val sum = marks.sumOf { it.VALUE }
                    result.add(0, sum.toFloat() / marks.size)
                    listOf(5, 4, 3, 2).forEachIndexed { index, value ->
                        separateMarksResult[index].add(0, marks.filter { it.VALUE == value }.size.toFloat())
                    }
                    labels.add("$i week")
                    marks.retainAll {
                        val split = it.DATE.split('.')
                        calendar.apply {
                            set(Calendar.DAY_OF_MONTH, split[0].toInt())
                            set(Calendar.MONTH, split[1].toInt())
                            set(Calendar.YEAR, split[2].toInt())
                        }
                        (calendar.timeInMillis / 86400000).toInt() < lastDate
                    }
                    lastDate -= 7
                }
                return listOf(
                    AnalyticsData.LineCommon(result, labels),
                    AnalyticsData.PieMarks(fiveCount, fourCount, threeCount, twoCount),
                    AnalyticsData.LineMarks(
                        separateMarksResult[0],
                        separateMarksResult[1],
                        separateMarksResult[2],
                        separateMarksResult[3],
                        labels
                    )
                )
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
                                    lesson.NAME,
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