package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import javax.inject.Inject

interface PerformanceCloudDataSource {
    suspend fun data(
        from: String,
        to: String,
    ): List<CloudLesson>

    suspend fun finalData(): List<PerformanceFinalLesson>

    suspend fun periods(): List<PerformancePeriod>

    class Base @Inject constructor(private val service: PerformanceService, private val eduUser: EduUser) :
        PerformanceCloudDataSource {
        override suspend fun data(
            from: String,
            to: String,
        ): List<CloudLesson> {
            val data = service.getMarks(
                PerformanceBody(
                    eduUser.apikey(),
                    eduUser.guid(),
                    from,
                    to,
                    ""
                )
            )
            return if (data.success)
                data.data
            else throw ServiceUnavailableException(data.message)
        }

        override suspend fun finalData(): List<PerformanceFinalLesson> {
            val data = service.getFinalMarks(
                PerformanceFinalBody(
                    eduUser.apikey(),
                    eduUser.guid(),
                    ""
                )
            )

            return if (data.success)
                data.data
            else throw ServiceUnavailableException(data.message)
        }

        override suspend fun periods(): List<PerformancePeriod> {
            val data = service.getPeriods(
                PerformanceFinalBody(
                    eduUser.apikey(),
                    eduUser.guid(),
                    ""
                )
            )

            return if (data.success)
                data.data
            else throw ServiceUnavailableException(data.message)
        }
    }
}