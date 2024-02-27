package com.maxim.diaryforstudents.performance.common.sl

import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.diary.data.DiaryDataToDomainMapper
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.data.DiaryService
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.HandleMarkType
import com.maxim.diaryforstudents.performance.common.data.HandleResponse
import com.maxim.diaryforstudents.performance.common.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.common.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.data.PerformanceService
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

interface MarksModule {
    fun marksInteractor(): PerformanceInteractor
    fun diaryRepository(): DiaryRepository

    class Base(private val core: Core) : MarksModule {
        private val diaryRepository by lazy {
            DiaryRepository.Base(
                core.retrofit().create(DiaryService::class.java),
                Formatter.Base,
                core.eduUser(),
                core,
                HandleMarkType.Base()
            )
        }

        private val marksInteractor by lazy {
            PerformanceInteractor.Base(
                PerformanceRepository.Base(
                    PerformanceCloudDataSource.Base(
                        core.retrofit().create(PerformanceService::class.java),
                        core.eduUser(),
                    ),
                    HandleResponse.Base(),
                    HandleMarkType.Base(),
                    core.performanceDatabase().dao(),
                ),
                core.simpleStorage(),
                FailureHandler.Base(),
                PerformanceDataToDomainMapper(),
                diaryRepository,
                DiaryDataToDomainMapper(PerformanceDataToDomainMapper())
            )
        }

        override fun marksInteractor() = marksInteractor
        override fun diaryRepository() = diaryRepository
    }
}