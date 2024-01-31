package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException

interface PerformanceCloudDataSource {
    suspend fun data(
        from: String,
        to: String,
    ): List<CloudLesson>

    suspend fun finalData(): List<PerformanceFinalLesson>

    class Base(private val service: PerformanceService, private val eduUser: EduUser) :
        PerformanceCloudDataSource {
        override suspend fun data(
            from: String,
            to: String,
        ): List<CloudLesson> {
            val data = service.getMarks(
                PerformanceBody(
                    BuildConfig.SHORT_API_KEY,
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
                    BuildConfig.SHORT_API_KEY,
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