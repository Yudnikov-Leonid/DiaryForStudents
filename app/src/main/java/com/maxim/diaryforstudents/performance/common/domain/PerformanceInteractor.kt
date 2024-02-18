package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType

interface PerformanceInteractor {
    suspend fun loadActualData()
    suspend fun loadFinalData()

    fun actualData(): List<PerformanceDomain>
    fun finalData(): List<PerformanceDomain>

    fun currentProgressType(): ProgressType
    fun currentQuarter(): Int
    fun getLessonByMark(lessonName: String, date: String): DiaryDomain.Lesson
    fun changeQuarter(quarter: Int)
}