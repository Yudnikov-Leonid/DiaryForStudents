package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.performance.analytics.data.AnalyticsData
import java.util.Calendar

interface HandleResponse {
    fun marks(
        lessons: List<CloudLesson>,
        calculateProgress: Boolean,
        quarter: Int
    ): List<PerformanceData.Lesson>

    fun finalMarks(lessons: List<PerformanceFinalLesson>): List<PerformanceData.Lesson>
    fun analytics(
        lessons: List<CloudLesson>,
        quarter: Int,
        lessonName: String,
        from: String,
        to: String,
        interval: Int
    ): List<AnalyticsData>

    fun finalAnalytics(
        lessons: List<PerformanceFinalLesson>,
        quarter: Int,
        actualQuarter: Int
    ): AnalyticsData

    class Base : HandleResponse {
        private val averageMap = mutableMapOf<Pair<String, Int>, Float>()

        override fun marks(
            lessons: List<CloudLesson>,
            calculateProgress: Boolean,
            quarter: Int
        ): List<PerformanceData.Lesson> {
            return lessons.filter { it.MARKS.isNotEmpty() }.map { lesson ->
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
        }

        override fun finalMarks(lessons: List<PerformanceFinalLesson>): List<PerformanceData.Lesson> {
            lessons.forEach { lesson ->
                lesson.PERIODS.forEachIndexed { i, period ->
                    averageMap[Pair(lesson.NAME, i + 1)] = period.AVERAGE
                }
            }

            return lessons.map { lesson ->
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
        }

        override fun analytics(
            lessons: List<CloudLesson>,
            quarter: Int,
            lessonName: String,
            from: String,
            to: String,
            interval: Int
        ): List<AnalyticsData> {
            val marks = mutableListOf<CloudMark>()
            lessons.forEach { lesson ->
                if (lessonName.isNotEmpty()) {
                    if (lesson.SUBJECT_NAME == lessonName)
                        marks.addAll(lesson.MARKS)
                } else
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
                var split = from.split('.')
                set(Calendar.DAY_OF_MONTH, split[0].toInt())
                set(Calendar.MONTH, split[1].toInt())
                set(Calendar.YEAR, split[2].toInt())
                firstDate = (timeInMillis / 86400000).toInt()
                split = to.split('.')
                set(Calendar.DAY_OF_MONTH, split[0].toInt())
                set(Calendar.MONTH, split[1].toInt())
                set(Calendar.YEAR, split[2].toInt())
                lastDate = (timeInMillis / 86400000).toInt()

            }
            val weeksCount = (lastDate - firstDate) / interval

            val result = mutableListOf<Float>()
            val separateMarksResult = listOf<java.util.ArrayList<Float>>(
                ArrayList(),
                ArrayList(),
                ArrayList(),
                ArrayList()
            )
            val labels = mutableListOf<String>()
            val labelText = when (interval) {
                in 1..6 -> "day"
                7 -> "week"
                else -> "month"
            }

            for (i in 1..weeksCount) {
                if (marks.size > 0) {
                    result.add(
                        0,
                        marks.sumOf { it.VALUE }.toFloat() / marks.size
                    )
                    listOf(5, 4, 3, 2).forEachIndexed { index, value ->
                        separateMarksResult[index].add(
                            0,
                            marks.filter { it.VALUE == value }.size.toFloat()
                        )
                    }
                    labels.add("$i $labelText")
                }
                marks.retainAll {
                    val split = it.DATE.split('.')
                    calendar.apply {
                        set(Calendar.DAY_OF_MONTH, split[0].toInt())
                        set(Calendar.MONTH, split[1].toInt())
                        set(Calendar.YEAR, split[2].toInt())
                    }
                    (calendar.timeInMillis / 86400000).toInt() < lastDate
                }
                lastDate -= interval
            }
            return listOf(
                AnalyticsData.LineCommon(result, labels, quarter, interval),
                AnalyticsData.PieMarks(fiveCount, fourCount, threeCount, twoCount),
                AnalyticsData.LineMarks(
                    separateMarksResult[0],
                    separateMarksResult[1],
                    separateMarksResult[2],
                    separateMarksResult[3],
                    labels
                )
            )
        }

        override fun finalAnalytics(
            lessons: List<PerformanceFinalLesson>,
            quarter: Int,
            actualQuarter: Int
        ): AnalyticsData {
            val resultMap = mutableMapOf<Int, Int>()
            lessons.forEach { lesson ->
                if (quarter != actualQuarter) {
                    if (quarter != 5) {
                        lesson.PERIODS[quarter - 1].MARK?.VALUE?.let {
                            resultMap[it] = resultMap[it]?.plus(1) ?: 1
                        }
                    } else {
                        lesson.PERIODS.forEach { period ->
                            period.MARK?.VALUE?.let {
                                resultMap[it] = resultMap[it]?.plus(1) ?: 1
                            }
                        }
                    }
                } else {
                    lesson.PERIODS[quarter - 1].AVERAGE.let {
                        val mark = when (it) {
                            in 2f..2.49f -> 2
                            in 2.5f..3.49f -> 3
                            in 3.5f..4.49f -> 4
                            else -> 5
                        }
                        resultMap[mark] = resultMap[mark]?.plus(1) ?: 1
                    }
                }
            }
            return AnalyticsData.PieFinalMarks(
                resultMap[5] ?: 0,
                resultMap[4] ?: 0,
                resultMap[3] ?: 0,
                resultMap[2] ?: 0
            )
        }
    }
}