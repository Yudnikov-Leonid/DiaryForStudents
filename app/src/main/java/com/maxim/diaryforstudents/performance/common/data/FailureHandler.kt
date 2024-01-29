package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.performance.common.domain.Failure
import com.maxim.diaryforstudents.performance.common.domain.NoInternetConnectionError
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableError
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import com.maxim.diaryforstudents.performance.common.domain.UnknownError
import java.net.UnknownHostException

interface FailureHandler {
    fun handle(e: Exception): Failure

    class Base: FailureHandler {
        override fun handle(e: Exception): Failure {
            return when(e) {
                is UnknownHostException -> NoInternetConnectionError()
                is ServiceUnavailableException -> ServiceUnavailableError(e.message ?: "Server error")
                is retrofit2.HttpException -> ServiceUnavailableError(e.message ?: "Server error")
                else -> UnknownError()
            }
        }
    }
}