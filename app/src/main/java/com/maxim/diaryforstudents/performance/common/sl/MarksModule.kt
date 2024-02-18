package com.maxim.diaryforstudents.performance.common.sl

import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.diary.data.DiaryDataToDomainMapper
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.data.DiaryService
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.HandleResponse
import com.maxim.diaryforstudents.performance.common.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.common.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.data.PerformanceService
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

interface MarksModule {
    fun marksInteractor(): PerformanceInteractor

    interface Clear {
        fun clear()
    }

    interface Mutable : MarksModule, Clear

    class Base(private val core: Core) : Mutable {
        private var marksInteractor: PerformanceInteractor? = null

        override fun marksInteractor(): PerformanceInteractor {
            if (marksInteractor == null) {
                marksInteractor = PerformanceInteractor.Base(
                    PerformanceRepository.Base(
                        PerformanceCloudDataSource.Base(
                            core.retrofit().create(PerformanceService::class.java),
                            core.eduUser(),
                        ),
                        HandleResponse.Base()
                    ),
                    core.simpleStorage(),
                    FailureHandler.Base(),
                    PerformanceDataToDomainMapper(),
                    DiaryRepository.Base(
                        core.retrofit().create(DiaryService::class.java),
                        Formatter.Base,
                        core.eduUser(),
                        core.simpleStorage()
                    ),
                    DiaryDataToDomainMapper(PerformanceDataToDomainMapper())
                )
            }
            return marksInteractor!!
        }

        override fun clear() {
            marksInteractor = null
        }
    }
}